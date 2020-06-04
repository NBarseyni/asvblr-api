package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TeamCategoryNotFoundException extends RuntimeException {
    public TeamCategoryNotFoundException(Long id) {
        super("Could not find team category with id = " + id);
    }
}

