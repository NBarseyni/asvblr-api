package com.pa.asvblrapi.exception;

public class PaymentModeNotFoundException extends RuntimeException{
    public PaymentModeNotFoundException(Long id) {
        super("Could not find paymentMode with id = " + id);
    }
}
