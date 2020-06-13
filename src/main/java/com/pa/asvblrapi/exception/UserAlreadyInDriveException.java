package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserAlreadyInDriveException extends RuntimeException {
    public UserAlreadyInDriveException(Long idDrive, Long idUser) {
        super(String.format("User with id = %s is already in drive with id = %s", idUser, idDrive));
    }
}

