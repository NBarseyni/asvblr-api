package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class JerseyNotFoundException extends RuntimeException {
    public JerseyNotFoundException(Long idTeam, Long idPlayer) {
        super(String.format("Could not find jersey with idTeam = %s and idPlayer = %s", idTeam, idPlayer));
    }
}

