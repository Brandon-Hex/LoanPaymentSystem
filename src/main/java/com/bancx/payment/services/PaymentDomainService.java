package com.bancx.payment.services;

import com.bancx.loan.entities.Loan;
import com.bancx.loan.services.LoanDomainService;
import com.bancx.payment.data.PaymentInDTO;
import com.bancx.payment.data.PaymentOutDTO;
import com.bancx.payment.entities.Payment;
import com.bancx.payment.exceptions.PaymentNotFoundException;
import com.bancx.payment.exceptions.PaymentTooLargeException;
import com.bancx.payment.repositories.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentDomainService {

    @Autowired
    private final PaymentRepository paymentRepository;

//    @Autowired
//    private final LoanDomainService loanDomainService;

    @Autowired
    private ModelMapper modelMapper;

    public PaymentDomainService(PaymentRepository paymentRepository, ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.modelMapper = modelMapper;
    }

    public PaymentOutDTO getPaymentById(long paymentId) {
        Payment foundPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));

        return modelMapper.map(foundPayment, PaymentOutDTO.class);
    }

    public Payment saveNewPayment(PaymentInDTO newPayment, Loan existingLoan) {
        if (newPayment.getPaymentAmount().compareTo(existingLoan.getLoanRemainingBalance()) > 0) {
            throw new PaymentTooLargeException(existingLoan.getLoanRemainingBalance());
        }

        Payment newPaymentEntity = new Payment(newPayment.getPaymentAmount(), existingLoan);
        return paymentRepository.save(newPaymentEntity);
    }

    public List<Payment> getPaymentsByLoanId(Long loanId) {
        return paymentRepository.findAllByLoanId(loanId);
    }
}
