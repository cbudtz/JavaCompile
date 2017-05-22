package rest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 22-05-2017.
 */
public class RunResult {
    private boolean success = false;
    private List<String> systemOut = new ArrayList<>();
    private List<String> systemErr = new ArrayList<>();

    public RunResult() {
    }

    public RunResult(boolean success, List<String> systemOut, List<String> systemErr) {
        this.success = success;
        this.systemOut = systemOut;
        this.systemErr = systemErr;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<String> getSystemOut() {
        return systemOut;
    }

    public void setSystemOut(List<String> systemOut) {
        this.systemOut = systemOut;
    }

    public List<String> getSystemErr() {
        return systemErr;
    }

    public void setSystemErr(List<String> systemErr) {
        this.systemErr = systemErr;
    }
}
