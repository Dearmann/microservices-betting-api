package com.github.dearmann.rateservice.exception;

import org.springframework.http.HttpStatus;

public class RatingException extends RuntimeException {

    private final HttpStatus httpStatus;

    public RatingException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
