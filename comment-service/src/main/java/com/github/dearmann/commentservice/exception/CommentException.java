package com.github.dearmann.commentservice.exception;

import org.springframework.http.HttpStatus;

public class CommentException extends RuntimeException {

    private final HttpStatus httpStatus;

    public CommentException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
