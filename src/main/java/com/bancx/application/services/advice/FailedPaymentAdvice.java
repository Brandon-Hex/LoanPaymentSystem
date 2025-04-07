package com.bancx.application.services.advice;

import com.bancx.payment.exceptions.FailedPaymentException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FailedPaymentAdvice {
    @ExceptionHandler(FailedPaymentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String loanNotFoundHandler(FailedPaymentException ex) {
        return ex.getMessage();
    }
}
