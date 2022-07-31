package com.dsib.language.core.training.application.file;

import lombok.Getter;

import java.io.InputStream;

public class FileTraining {

    private @Getter InputStream file;
    private @Getter String name;
    private @Getter int fileSize;
    private @Getter String fileExtension;

    public FileTraining(InputStream file, int fileSize, String name, String fileExtension) {
        this.file = file;
        this.name = name;
        this.fileSize = fileSize;
        this.fileExtension = fileExtension;
    }
}
