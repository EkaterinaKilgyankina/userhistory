package com.insite.userhistory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyExistsException extends RuntimeException{
    private static final String MESSAGE = "object already exists";

    public AlreadyExistsException() {
        super(MESSAGE);
    }

}
