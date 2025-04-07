package com.bancx.payment;

import com.bancx.loan.entities.Loan;
import com.bancx.payment.data.PaymentInDTO;
import com.bancx.payment.data.PaymentOutDTO;
import com.bancx.payment.entities.Payment;
import com.bancx.payment.exceptions.PaymentNotFoundException;
import com.bancx.payment.repositories.PaymentRepository;
import com.bancx.payment.services.PaymentDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PaymentTests {
    private PaymentRepository paymentRepository;
    private PaymentDomainService paymentService;

    @BeforeEach
    public void setup() {
        ModelMapper modelMapper = new ModelMapper();
        paymentRepository = mock(PaymentRepository.class);

        paymentService = new PaymentDomainService(paymentRepository, modelMapper);
    }

    @Test
    public void testGetPaymentById_shouldReturnPaymentDTO() {
        //given
        Payment payment = new Payment(new BigDecimal("100.00"), new Loan());
        payment.setPaymentId(1L);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        //when
        PaymentOutDTO result = paymentService.getPaymentById(1L);

        //then
        assertNotNull(result);
        assertEquals(new BigDecimal("100.00"), result.getPaymentAmount());
    }

    @Test
    public void testGetPaymentById_shouldThrowException_whenNotFound() {
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class, () -> paymentService.getPaymentById(999L));
    }

    @Test
    public void testSaveNewPayment_shouldSavePayment_whenValid() {
        //given
        Loan loan = new Loan(new BigDecimal("1000.00"), 12);
        loan.setLoanId(1L);
        loan.setLoanRemainingBalance(new BigDecimal("800.00"));

        PaymentInDTO paymentInDTO = new PaymentInDTO();
        paymentInDTO.setLoanId(1L);
        paymentInDTO.setPaymentAmount(new BigDecimal("200.00"));

        //when
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Payment savedPayment = paymentService.saveNewPayment(paymentInDTO, loan);

        //then
        assertNotNull(savedPayment);
        assertEquals(new BigDecimal("200.00"), savedPayment.getPaymentAmount());
        assertEquals(loan, savedPayment.getLoan());
    }
}

