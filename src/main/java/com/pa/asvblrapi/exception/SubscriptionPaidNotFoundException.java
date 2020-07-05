package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubscriptionPaidNotFoundException extends RuntimeException {
    public SubscriptionPaidNotFoundException(Long idSubscription, Long idPaymentMode) {
        super(String.format("The subscription with id = %s doesn't have payment mode with id = %s", idSubscription, idPaymentMode));
    }
}
