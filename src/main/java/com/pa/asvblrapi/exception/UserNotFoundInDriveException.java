package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundInDriveException extends RuntimeException {
    public UserNotFoundInDriveException(Long idDrive, Long idUser) {
        super(String.format("Could not find user with id = %s in drive with id = %s", idUser, idDrive));
    }
}

