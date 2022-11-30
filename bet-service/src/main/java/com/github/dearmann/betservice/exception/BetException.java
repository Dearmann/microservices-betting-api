package com.github.dearmann.betservice.exception;

import org.springframework.http.HttpStatus;

public class BetException extends RuntimeException {

    private final HttpStatus httpStatus;

    public BetException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
