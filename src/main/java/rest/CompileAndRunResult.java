package rest;

import java.util.List;

/**
 * Created by Christian on 22-05-2017.
 */
public class CompileAndRunResult {
    private boolean compilationSuccess;
    private List<CompilationResult> compilationResult;
    private RunResult runResult;

    public CompileAndRunResult() {
    }

    public CompileAndRunResult(boolean compilationSuccess, List<CompilationResult> compilationResult, RunResult runResult) {
        this.compilationSuccess = compilationSuccess;
        this.compilationResult = compilationResult;
        this.runResult = runResult;
    }

    public List<CompilationResult> getCompilationResult() {
        return compilationResult;
    }

    public void setCompilationResult(List<CompilationResult> compilationResult) {
        this.compilationResult = compilationResult;
    }

    public RunResult getRunResult() {
        return runResult;
    }

    public void setRunResult(RunResult runResult) {
        this.runResult = runResult;
    }
}
