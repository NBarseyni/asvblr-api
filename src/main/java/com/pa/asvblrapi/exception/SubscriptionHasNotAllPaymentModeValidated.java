package com.pa.asvblrapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubscriptionHasNotAllPaymentModeValidated extends RuntimeException {
    public SubscriptionHasNotAllPaymentModeValidated(Long idSubscription) {
        super(String.format("The subscription with id = %s hasn't all payment mode validated", idSubscription));
    }
}
