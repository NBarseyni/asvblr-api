package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubscriptionCategoryNotFoundException extends RuntimeException {
    public SubscriptionCategoryNotFoundException(Long id) {
        super("Could not find subscription category with id = " + id);
    }
}
