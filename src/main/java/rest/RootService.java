package rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import javax.tools.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Christian on 19-05-2017.
 */
@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RootService {

    @GET
    @Path("template")
    @Operation(description = "Get a sample template to try out compilation")
    public CompilePack getTemplate() throws IOException {
        CompilePack compilePack = new CompilePack();
        compilePack.setMainClass("Main");
        FileRepresentation fileRepresentation = new FileRepresentation();
        fileRepresentation.setFileName("Main.java");
        fileRepresentation.setFileContents(mainContents());
        compilePack.addFile(fileRepresentation);
        return compilePack;
    }

    @POST
    @Path("compile")
    @Operation(description = "Post code to compile. Try GET /template to get an example")
    public CompileAndRunResult getRoot(CompilePack code) throws IOException, InterruptedException {
        for (FileRepresentation codeFile : code.getFiles()) {
            System.out.println(codeFile.getFileContents());
        }

        boolean allSuccess = true;
        List<CompilationResult> results = new ArrayList<>();
        for (FileRepresentation rep : code.getFiles()) {
            List<String> lines = new ArrayList<>();
            lines.add(rep.getFileContents());
            Files.write(Paths.get(rep.getFileName()), lines);
            CompilationResult result = javaCompile(rep.getFileName());
            results.add(result);
            if (!result.isSuccess()) allSuccess = false;
        }
        RunResult runResult = null;

        if (allSuccess) {
            runResult = javaRun(code);
        }

        CompileAndRunResult compileAndRunResult = new CompileAndRunResult(allSuccess, results, runResult);
        return compileAndRunResult;

    }

    private CompilationResult javaCompile(String fileName) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticCollector, null, StandardCharsets.UTF_8);
        Iterable<? extends JavaFileObject> javaFileObjectsFromStrings = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(fileName));

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector, null, null, javaFileObjectsFromStrings);
        Boolean callSuccess = task.call();
        CompilationResult compilationResult = new CompilationResult();
        compilationResult.setSuccess(callSuccess);
        List<DiagnosticResult> diagnosticResult = getDiagnosticResults(diagnosticCollector);
        compilationResult.setDiagnostics(diagnosticResult);
        System.out.println(callSuccess);
        System.out.println(diagnosticCollector.getDiagnostics().size());
        return compilationResult;
    }

    private RunResult javaRun(CompilePack pack) throws IOException, InterruptedException {
        //Start process
        Process proc = Runtime.getRuntime().exec("java -cp . " + pack.getMainClass());
        // Setup Streams
        System.out.println("Running");
        InputStream errorStream = proc.getErrorStream(); //Error Output from Process
        InputStream inputStream = proc.getInputStream(); //Output from Process

        OutputStream outputStream = proc.getOutputStream(); //Input to Process
        //Attach readers and writers
        BufferedReader errorOutput = new BufferedReader(new InputStreamReader(errorStream));
        BufferedReader output = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter writer = new PrintWriter(outputStream);

        List<String> errorStrings = new ArrayList<>();
        List<String> outStrings = new ArrayList<>();
        String currentError = null;
        String currentOut = null;


        RunResult runResult = new RunResult();
        System.out.println("Witing for process");
        runResult.setSuccess(proc.waitFor(2, TimeUnit.SECONDS));
        System.out.println("Done waiting");
        if (!runResult.isSuccess()) {
            proc.destroyForcibly();
            runResult.getSystemErr().add("JavaRunner: Process didn't finish within 2 seconds");
        } else {
            while ((currentError = errorOutput.readLine()) != null ||
                    (currentOut = output.readLine()) != null) {
                if (currentError != null && !currentError.contains("Picked up JAVA_TOOL_OPTIONS"))
                    errorStrings.add(currentError);
                if (currentOut != null) outStrings.add(currentOut);
            }
            runResult.setSystemErr(errorStrings);
            runResult.setSystemOut(outStrings);
        }
        return runResult;
    }

    private ArrayList<DiagnosticResult> getDiagnosticResults(DiagnosticCollector<JavaFileObject> diagnosticCollector) {
        List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticCollector.getDiagnostics();
        ArrayList<DiagnosticResult> resultList = new ArrayList<>();
        for (Diagnostic diagnostic : diagnostics) {
            DiagnosticResult result = new DiagnosticResult();
            result.setMessage(diagnostic.getMessage(null));
            result.setLineNumber(diagnostic.getLineNumber());
            result.setColumn(diagnostic.getColumnNumber());
            result.setStartPosition(diagnostic.getStartPosition());
            result.setEndPosition(diagnostic.getEndPosition());
            result.setKind(diagnostic.getKind());
            result.setPosition(diagnostic.getPosition());
            resultList.add(result);
        }
        return resultList;
    }

    private String javaCompileAndRun(String fileName) throws IOException, InterruptedException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticCollector, null, StandardCharsets.UTF_8);
        Iterable<? extends JavaFileObject> javaFileObjectsFromStrings = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(fileName));

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector, null, null, javaFileObjectsFromStrings);
        Boolean callSuccess = task.call();
        System.out.println(callSuccess);

        System.out.println(diagnosticCollector.getDiagnostics().size());

        if (callSuccess) {
            Process proc = Runtime.getRuntime().exec("java -cp . filetofind");


            InputStream errin = proc.getErrorStream();
            InputStream in = proc.getInputStream();
            BufferedReader errorOutput = new BufferedReader(new InputStreamReader(errin));
            BufferedReader output = new BufferedReader(new InputStreamReader(in));

            StringBuilder out = new StringBuilder();
            StringBuilder error = new StringBuilder();
            try {
                String line1 = null;
                String line2 = null;
                while ((line1 = errorOutput.readLine()) != null ||
                        (line2 = output.readLine()) != null) {
                    System.out.println(line1);
                    if (line1 != null && !(line1.contains("Picked up JAVA_TOOL_OPTIONS")))
                        error.append("error:" + line1 + "\r\n");
                    if (line2 != null) out.append("out: " + line2 + "\r\n");
                }//end while
                errorOutput.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }//end catc
            boolean result = proc.waitFor(2, TimeUnit.SECONDS);

            if (!result) {
                return "process failed to run within 2 seconds - try again!";
            }
            return "Out: " + out.toString() + ", Error: " + error.toString();

        } else {
            StringBuilder builder = new StringBuilder();

            for (Diagnostic diagnostic : diagnosticCollector.getDiagnostics()) {
                String code = "Diagnostic code: " + diagnostic.getCode() + "\r\n";

                builder.append(code);
                builder.append("Diagnostic message:" + diagnostic.getMessage(null) + "\r\n");
                builder.append("Error at Line " + diagnostic.getLineNumber() + ", Position " + diagnostic.getColumnNumber() + "\r\n");
                builder.append("StartPosition" + diagnostic.getStartPosition() + "\r\n");
                builder.append("EndPosition: " + diagnostic.getEndPosition() + "\r\n");

                builder.append("Source: " + diagnostic.getSource() + "\r\n");
                builder.append("---------" + "\r\n");
            }
            return builder.toString();
        }
    }

    private String mainContents() {
        StringBuilder builder = new StringBuilder();
        builder.append("public class Main {");
        builder.append("\tpublic static void main(String[] args){");
        builder.append("\t\tSystem.out.println(\"Hello World!\");");
        builder.append("\t}");
        builder.append("}");
        return builder.toString();
    }

}

