package com.bancx.payment.exceptions;

public class FailedPaymentException extends  RuntimeException{
    public FailedPaymentException(String errMessage) {
        super(errMessage);
    }
}
