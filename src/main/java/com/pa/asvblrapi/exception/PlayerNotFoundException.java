package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(Long id) {
        super("Could not find player with id = " + id);
    }

    public PlayerNotFoundException(Long idUser, int i) {
        super(String.format("User with id = %s is not a player", idUser));
    }
}
