package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserIsNotPlayerException extends RuntimeException {
    public UserIsNotPlayerException(Long idUser) {
        super(String.format("User with id = %s isn't a player", idUser));
    }
}
