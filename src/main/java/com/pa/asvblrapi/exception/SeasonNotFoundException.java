package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SeasonNotFoundException extends RuntimeException {
    public SeasonNotFoundException(Long id) {
        super("Could not find season with id = " + id);
    }

    public SeasonNotFoundException() {
        super("No current season");
    }
}
