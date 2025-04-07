package com.bancx.application.services.advice;

import com.bancx.payment.exceptions.PaymentNotFoundException;
import com.bancx.payment.exceptions.PaymentTooLargeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PaymentControllerAdvice {
    @ExceptionHandler(PaymentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String paymentNotFoundHandler(PaymentNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(PaymentTooLargeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String paymentTooLargeHandler(PaymentTooLargeException ex) {
        return ex.getMessage();
    }
}
