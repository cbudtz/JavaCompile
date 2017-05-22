package rest;

import javax.tools.Diagnostic;

/**
 * Created by Christian on 22-05-2017.
 */
public class DiagnosticResult {
    private String message;
    private long lineNumber;
    private long column;
    private long startPosition;
    private long endPosition;
    private Diagnostic.Kind kind;
    private long position;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLineNumber(long lineNumber) {
        this.lineNumber = lineNumber;
    }

    public long getLineNumber() {
        return lineNumber;
    }

    public void setColumn(long column) {
        this.column = column;
    }

    public long getColumn() {
        return column;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setEndPosition(long endPosition) {
        this.endPosition = endPosition;
    }

    public long getEndPosition() {
        return endPosition;
    }

    public void setKind(Diagnostic.Kind kind) {
        this.kind = kind;
    }

    public Diagnostic.Kind getKind() {
        return kind;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public long getPosition() {
        return position;
    }
}
