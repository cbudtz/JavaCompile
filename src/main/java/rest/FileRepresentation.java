package rest;

/**
 * Created by Christian on 22-05-2017.
 */
public class FileRepresentation {
    private String fileName;
    private String fileContents;

    public FileRepresentation(String fileName, String fileContents) {
        this.fileName = fileName;
        this.fileContents = fileContents;
    }

    public FileRepresentation() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContents() {
        return fileContents;
    }

    public void setFileContents(String fileContents) {
        this.fileContents = fileContents;
    }
}
