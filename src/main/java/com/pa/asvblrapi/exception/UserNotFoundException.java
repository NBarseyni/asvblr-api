package com.pa.asvblrapi.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("Count not find user " + id);
    }
}
