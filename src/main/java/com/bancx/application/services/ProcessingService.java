package com.bancx.application.services;

import com.bancx.application.data.LoanWithPaymentsDTO;
import com.bancx.loan.entities.Loan;
import com.bancx.loan.services.LoanDomainService;
import com.bancx.payment.data.PaymentInDTO;
import com.bancx.payment.data.PaymentOutDTO;
import com.bancx.payment.entities.Payment;
import com.bancx.payment.services.PaymentDomainService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProcessingService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PaymentDomainService paymentService;

    @Autowired
    private LoanDomainService loanService;

    public ProcessingService(PaymentDomainService paymentDomainService, ModelMapper modelMapper, LoanDomainService loanDomainService) {
        this.paymentService = paymentDomainService;
        this.modelMapper = modelMapper;
        this.loanService = loanDomainService;
    }

    @Transactional
    public Payment makePayment(PaymentInDTO newPayment) {
        Loan loan = loanService.getLoanById(newPayment.getLoanId());

        Payment payment = paymentService.saveNewPayment(newPayment, loan);
        Loan updatedLoan = loanService.processPayment(loan.getLoanId(), payment.getPaymentAmount());

        return payment;
    }

    public LoanWithPaymentsDTO getLoanWithPayments(Long loanId) {
        Loan loan = loanService.getLoanById(loanId);
        List<Payment> payments = paymentService.getPaymentsByLoanId(loanId);

        LoanWithPaymentsDTO output = modelMapper.map(loan, LoanWithPaymentsDTO.class);
        output.setPayments(payments.stream()
                .map(payment -> modelMapper.map(payment, PaymentOutDTO.class))
                .toList());

        return output;
    }
}
