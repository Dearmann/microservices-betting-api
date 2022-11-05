package com.github.dearmann.matchservice.exception;

import org.springframework.http.HttpStatus;

public class BadEntityIdException extends RuntimeException {

    private final HttpStatus httpStatus;

    public BadEntityIdException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
