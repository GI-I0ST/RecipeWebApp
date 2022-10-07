package com.ghost.recipewebapp.exception;

public class FileLoaderIOException extends FileLoaderException {

    public FileLoaderIOException(String message) {
        super(message);
    }

    public FileLoaderIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
