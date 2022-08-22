package com.ghost.recipewebapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FileUploadException extends RuntimeException {
    public FileUploadException(Throwable err) {
        super(err);
    }

    public FileUploadException(String msg, Throwable err) {
        super(msg, err);
    }
}
