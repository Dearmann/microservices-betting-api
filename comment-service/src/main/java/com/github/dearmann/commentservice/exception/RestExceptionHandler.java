package com.github.dearmann.commentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(BadEntityIdException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(exception.getHttpStatus().value())
                .message(exception.getMessage())
                .timeStamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, exception.getHttpStatus());
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(NumberFormatException exception) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .message("Bad Request")
                .timeStamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
