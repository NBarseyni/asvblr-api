package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DriveNotFoundException extends RuntimeException {
    public DriveNotFoundException(Long id) {
        super("Could not find drive with id = " + id);
    }
}

