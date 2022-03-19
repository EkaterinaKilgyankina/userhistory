package com.insite.userhistory.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleExceptions(NotFoundException ex) {
        return ResponseEntity.status(NOT_FOUND).body(new ErrorMessage(ex.getMessage()));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Object> handleExceptions(AlreadyExistsException ex) {
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorMessage(ex.getMessage()));
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        var value = ex.getAllErrors()
                .stream()
                .findFirst()
                .map(err -> new ErrorMessage(err.getDefaultMessage()))
                .orElse(null);

        return ResponseEntity.status(BAD_REQUEST).body(value);
    }
}

