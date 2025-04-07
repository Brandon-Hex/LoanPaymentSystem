package com.bancx.payment.services;

import com.bancx.loan.entities.Loan;
import com.bancx.loan.repositories.LoadDatabase;
import com.bancx.payment.data.PaymentInDTO;
import com.bancx.payment.data.PaymentOutDTO;
import com.bancx.payment.entities.Payment;
import com.bancx.payment.exceptions.FailedPaymentException;
import com.bancx.payment.exceptions.PaymentNotFoundException;
import com.bancx.payment.exceptions.PaymentTooLargeException;
import com.bancx.payment.repositories.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PaymentDomainService {
    private static final Logger log = LoggerFactory.getLogger(PaymentDomainService.class);

    @Autowired
    private final PaymentRepository paymentRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PaymentDomainService(PaymentRepository paymentRepository, ModelMapper modelMapper) {
        this.paymentRepository = paymentRepository;
        this.modelMapper = modelMapper;
    }

    public Payment getPaymentById(long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
    }

    public Payment saveNewPayment(PaymentInDTO newPayment, Loan existingLoan) {
        if (newPayment.getPaymentAmount().compareTo(existingLoan.getLoanRemainingBalance()) > 0) {
            throw new PaymentTooLargeException(existingLoan.getLoanRemainingBalance());
        }

        try {
            Payment newPaymentEntity = new Payment(newPayment.getPaymentAmount(), existingLoan);
            return paymentRepository.save(newPaymentEntity);
        } catch (Exception ex) {
            log.error(Arrays.toString(ex.getStackTrace()));

            //implement specific logic to re-try payment, or performing alerting on failed payment
            throw new FailedPaymentException("Failed to process payment");
        }
    }

    public List<Payment> getPaymentsByLoanId(Long loanId) {
        return paymentRepository.findAllByLoanId(loanId);
    }
}
