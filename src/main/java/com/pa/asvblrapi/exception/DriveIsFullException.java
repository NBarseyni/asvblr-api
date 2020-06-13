package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DriveIsFullException extends RuntimeException {
    public DriveIsFullException(Long id) {
        super(String.format("The drive with id = %s is full.", id));
    }
}

