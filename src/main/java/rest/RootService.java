package rest;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.omg.SendingContext.RunTime;

import javax.tools.*;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
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
public class RootService {

    @POST
    public String getRoot(String code) {
        System.out.println("code: " + code);
        List<String> lines = new ArrayList<>();
        lines.add(code);
        try {
            Files.write(Paths.get("filetofind.java"), lines);
            return javaCompile("filetofind.java");
        } catch (IOException e  ) {
            e.printStackTrace();
            System.out.println("failed");
        } catch (InterruptedException e) {
            return "Something interrupted the run of the code.";
        }

        return "test";
    }

    private String javaCompile(String fileName) throws IOException, InterruptedException {
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
                String line1 =null;
                String line2 = null;
                while ((line1 = errorOutput.readLine()) != null ||
                        (line2 = output.readLine()) != null) {
                    if (line1 != null) error.append("error:" + line1 + "\r\n");
                    if (line2 != null) out.append("out: " +line2+ "\r\n");
                }//end while
                errorOutput.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }//end catc
            boolean result = proc.waitFor(2, TimeUnit.SECONDS);

            if (!result){
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

                builder.append("Source: " +diagnostic.getSource()+"\r\n");
                builder.append("---------" + "\r\n");
            }
        return builder.toString();
        }
    }

}

