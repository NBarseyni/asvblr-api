package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClothingSizeNotFoundException extends RuntimeException {
    public ClothingSizeNotFoundException(Long id) {
        super("Could not find clothing size with id = " + id);
    }
}
