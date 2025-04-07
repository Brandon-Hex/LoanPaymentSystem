package com.bancx.payment.exceptions;

import java.math.BigDecimal;

public class PaymentTooLargeException extends RuntimeException {
    public PaymentTooLargeException(BigDecimal remainingLoanBalance) {
        super("Payment amount cannot be larger than remaining Loan Balance of " + remainingLoanBalance);
    }
}
