package com.rikkie.noteapp.model;

/**
 * Created by tuyenpx on 28/04/2016.
 */
public class ImageItem {
    private String pathFile;

    public ImageItem(String pathFile) {
        this.pathFile = pathFile;
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    @Override
    public String toString() {
        return "[Path file ] = " + pathFile;
    }
}
