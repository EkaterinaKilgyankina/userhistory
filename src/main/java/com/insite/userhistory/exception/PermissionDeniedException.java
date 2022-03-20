package com.insite.userhistory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PermissionDeniedException extends RuntimeException {
    private static final String MESSAGE = "permission denied, token or name is not valid";

    public PermissionDeniedException() {
        super(MESSAGE);
    }
}

