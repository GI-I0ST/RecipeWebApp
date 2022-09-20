package com.ghost.recipewebapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileLoaderException extends RuntimeException{
    public FileLoaderException(String message) {
        super(message);
    }

    public FileLoaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileLoaderException(Throwable cause) {
        super(cause);
    }
}
