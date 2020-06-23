package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MatchDidNotTakePlaceException extends RuntimeException{
    public MatchDidNotTakePlaceException(Long id) {
        super(String.format("Match with id = %s did not take place yet", id));
    }
}
