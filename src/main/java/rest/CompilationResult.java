package rest;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.List;

/**
 * Created by Christian on 22-05-2017.
 */
public class CompilationResult {
    private boolean success;
    private List<DiagnosticResult> diagnostics;

    public CompilationResult(boolean success) {
        this.success = success;
    }

    public CompilationResult() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setDiagnostics(List<DiagnosticResult> diagnostics) {
        this.diagnostics = diagnostics;
    }

    public List<DiagnosticResult> getDiagnostics() {
        return diagnostics;
    }
}
