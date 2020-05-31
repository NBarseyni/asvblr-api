package com.pa.asvblrapi.exception;

public class InvalidOldPasswordException extends RuntimeException {
    public InvalidOldPasswordException() {
        super("Invalid old password");
    }
}
