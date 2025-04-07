package com.bancx.loan.exceptions;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(Long loanId) {
        super("Could not find Loan with ID " + loanId);
    }
}
