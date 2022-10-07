package com.ghost.recipewebapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessDeniedException extends org.springframework.security.access.AccessDeniedException {
    public AccessDeniedException(String msg) {
        super(msg);
    }

    public AccessDeniedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
