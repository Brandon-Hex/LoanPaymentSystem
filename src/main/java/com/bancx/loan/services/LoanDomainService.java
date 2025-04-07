package com.bancx.loan.services;

import com.bancx.loan.constants.Constants;
import com.bancx.loan.data.LoanInDTO;
import com.bancx.loan.data.LoanOutDTO;
import com.bancx.loan.entities.Loan;
import com.bancx.loan.exceptions.LoanNotFoundException;
import com.bancx.loan.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class LoanDomainService {

    @Autowired
    private final LoanRepository loanRepository;

    public LoanDomainService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public Loan getLoanById(long loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException(loanId));
    }

    public Loan saveNewLoan(LoanInDTO newLoan) {
        Loan newLoanEntity = new Loan(newLoan.getLoanAmount(), newLoan.getTerm());
        loanRepository.save(newLoanEntity);

        return newLoanEntity;
    }

    public Loan processPayment(Long loanId, BigDecimal paymentAmount) {
        Loan existingLoan = this.getLoanById(loanId);

        BigDecimal newLoanBalance = existingLoan.getLoanRemainingBalance().subtract(paymentAmount);
        existingLoan.setLoanRemainingBalance(newLoanBalance);

        if (newLoanBalance.equals(new BigDecimal("0.00"))) {
            existingLoan.setStatus(Constants.LoanStatus.SETTLED);
        }

        loanRepository.save(existingLoan);

        return existingLoan;
    }
}
