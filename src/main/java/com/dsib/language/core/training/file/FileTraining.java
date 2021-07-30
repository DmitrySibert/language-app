package com.dsib.language.core.training.file;

import java.io.InputStream;

public class FileTraining {

    private InputStream file;
    private String name;
    private int fileSize;
    private String fileExtension;

    public FileTraining(InputStream file, int fileSize, String name, String fileExtension) {
        this.file = file;
        this.name = name;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
    }

    public InputStream getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public int getFileSize() {
        return fileSize;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}
