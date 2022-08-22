package com.ghost.recipewebapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class EnvVarNotFoundException extends RuntimeException{
    public EnvVarNotFoundException(String message) {
        super(message);
    }

    public EnvVarNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EnvVarNotFoundException(Throwable cause) {
        super(cause);
    }
}
