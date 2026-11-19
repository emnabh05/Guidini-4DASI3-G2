package com.guideme.ms3_reservations_payments.exception;

import org.springframework.http.HttpStatus;

public class PaymentRefusedException extends ApiException {

    public PaymentRefusedException(String message) {
        super(HttpStatus.PAYMENT_REQUIRED, message);
    }
}
