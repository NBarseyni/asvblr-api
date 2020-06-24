package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserIsPresidentException extends RuntimeException {
    public UserIsPresidentException(Long idUser) {
        super(String.format("User with id = %s is president, you can't delete it", idUser));
    }
}

