package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserIsPlayerException extends RuntimeException {
    public UserIsPlayerException(Long idUser) {
        super(String.format("User with id = %s is a player, you can't delete it", idUser));
    }
}
