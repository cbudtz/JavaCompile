package rest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christian on 22-05-2017.
 */

public class CompilePack {
    private String mainClass;
    private List<FileRepresentation> files = new ArrayList<>();//FileName
    private List<String> input = new ArrayList<>();

    public CompilePack(String mainClass, List<FileRepresentation> files, List<String> input) {
        this.mainClass = mainClass;
        this.files = files;
        this.input = input;
    }

    public CompilePack() {

    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public void addFile(FileRepresentation representation) {
        this.files.add(representation);
    }

    public List<FileRepresentation> getFiles() {
        return files;
    }

    public void setFiles(List<FileRepresentation> files) {
        this.files = files;
    }

    public List<String> getInput() {
        return input;
    }

    public void setInput(List<String> input) {
        this.input = input;
    }
}
