package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SubscriptionAlreadyValidatedException extends RuntimeException {
    public SubscriptionAlreadyValidatedException() { super("This subscription is already validated"); }
}
