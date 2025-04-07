package com.bancx.payment.exceptions;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(Long paymentId) {
        super("Could not find payment with ID " + paymentId);
    }
}
