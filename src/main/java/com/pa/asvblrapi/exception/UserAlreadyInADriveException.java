package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyInADriveException extends RuntimeException {
    public UserAlreadyInADriveException(Long idUser) {
        super(String.format("User with id = %s is already in a drive for this match and this drive", idUser));
    }
}
