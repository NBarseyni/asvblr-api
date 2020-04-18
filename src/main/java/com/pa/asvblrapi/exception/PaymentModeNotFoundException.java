package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PaymentModeNotFoundException extends RuntimeException{
    public PaymentModeNotFoundException(Long id) {
        super("Could not find paymentMode with id = " + id);
    }
}
