package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyManagerException extends RuntimeException {
    public UserAlreadyManagerException(Long idUser) {
        super(String.format("User with id = %s is already manager", idUser));
    }
}
