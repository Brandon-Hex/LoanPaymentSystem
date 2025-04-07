package com.bancx.application;

import com.bancx.application.data.LoanWithPaymentsDTO;
import com.bancx.application.services.ProcessingService;
import com.bancx.loan.constants.Constants;
import com.bancx.loan.entities.Loan;
import com.bancx.loan.exceptions.LoanNotFoundException;
import com.bancx.loan.repositories.LoanRepository;
import com.bancx.loan.services.LoanDomainService;
import com.bancx.payment.data.PaymentInDTO;
import com.bancx.payment.entities.Payment;
import com.bancx.payment.exceptions.PaymentTooLargeException;
import com.bancx.payment.repositories.PaymentRepository;
import com.bancx.payment.services.PaymentDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class ApplicationTests {
    private LoanRepository loanRepository;
    private PaymentRepository paymentRepository;

    private LoanDomainService loanService;
    private PaymentDomainService paymentService;
    private ModelMapper modelMapper;
    private ProcessingService processingService;


    @BeforeEach
    public void setup() {
        modelMapper = new ModelMapper();

        loanRepository = mock(LoanRepository.class);
        paymentRepository = mock(PaymentRepository.class);

        loanService = new LoanDomainService(loanRepository);
        paymentService = new PaymentDomainService(paymentRepository, modelMapper);
        processingService = new ProcessingService(paymentService, modelMapper, loanService);
    }

    @Test
    public void testMakePayment_shouldProcessPaymentAndUpdateLoan() {
        Long loanId = 1L;
        BigDecimal loanAmount = new BigDecimal("1000.00");
        BigDecimal paymentAmount = new BigDecimal("200.00");

        Loan loan = new Loan(loanAmount, 12);
        loan.setLoanId(loanId);

        PaymentInDTO paymentInDTO = new PaymentInDTO();
        paymentInDTO.setLoanId(loanId);
        paymentInDTO.setPaymentAmount(paymentAmount);

        Payment payment = new Payment(paymentAmount, loan);
        payment.setPaymentId(1L);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment savedPayment = processingService.makePayment(paymentInDTO);

        assertNotNull(savedPayment);
        assertEquals(paymentAmount, savedPayment.getPaymentAmount());
        assertEquals(loanId, savedPayment.getLoan().getLoanId());
        assertEquals(new BigDecimal("800.00"), loan.getLoanRemainingBalance());
    }

    @Test
    public void testMakePayment_shouldSetLoanToSettled_whenFullyPaid() {
        //given
        Long loanId = 2L;
        BigDecimal loanAmount = new BigDecimal("500");

        Loan loan = new Loan(loanAmount, 6);
        loan.setLoanId(loanId);

        PaymentInDTO paymentInDTO = new PaymentInDTO();
        paymentInDTO.setLoanId(loanId);
        paymentInDTO.setPaymentAmount(loanAmount);

        Payment payment = new Payment(loanAmount, loan);
        payment.setPaymentId(2L);

        //when
        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));
        processingService.makePayment(paymentInDTO);

        //then
        assertEquals(new BigDecimal("0.00"), loan.getLoanRemainingBalance());
        assertEquals(Constants.LoanStatus.SETTLED, loan.getStatus());
    }

    @Test
    public void testMakePayment_shouldThrowException_whenLoanNotFound() {
        //given
        Long invalidLoanId = 99L;
        PaymentInDTO dto = new PaymentInDTO();
        dto.setLoanId(invalidLoanId);
        dto.setPaymentAmount(BigDecimal.TEN);

        //when
        when(loanRepository.findById(invalidLoanId)).thenReturn(Optional.empty());

        //then
        Exception exception = assertThrows(RuntimeException.class, () -> processingService.makePayment(dto));
        assertTrue(exception.getMessage().contains("Could not find Loan with ID"));
    }

    @Test
    public void testMakePayment_shouldNotAllowOverpayment() {
        //given
        Long loanId = 4L;
        Loan loan = new Loan(new BigDecimal("100.00"), 6);
        loan.setLoanId(loanId);
        loan.setLoanRemainingBalance(new BigDecimal("50.00"));

        PaymentInDTO dto = new PaymentInDTO();
        dto.setLoanId(loanId);
        dto.setPaymentAmount(new BigDecimal("100.00"));

        //when
        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));

        //then
        Exception exception = assertThrows(PaymentTooLargeException.class, () -> processingService.makePayment(dto));
        assertEquals("Payment amount cannot be larger than remaining Loan Balance of 50.00", exception.getMessage());
    }

    @Test
    public void testMakePayment_shouldThrowException_whenPaymentIsNull() {
        Exception exception = assertThrows(NullPointerException.class, () -> processingService.makePayment(null));
        assertNotNull(exception);
    }

    @Test
    public void testGetLoanWithPayments_shouldThrowException_whenLoanNotFound() {
        Long loanId = 404L;
        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(LoanNotFoundException.class, () -> processingService.getLoanWithPayments(loanId));
        assertTrue(exception.getMessage().contains("Could not find Loan with ID 404"));
    }

    @Test
    public void testGetLoanWithPayments_shouldReturnLoanWithPayments() {
        //given
        Long loanId = 1L;
        Loan loan = new Loan(new BigDecimal("1000.00"), 12);
        loan.setLoanId(loanId);
        loan.setLoanRemainingBalance(new BigDecimal("800.00"));

        Payment payment1 = new Payment(new BigDecimal("200.00"), loan);
        payment1.setPaymentId(1L);
        payment1.setPaymentDateTime(new Date());

        Payment payment2 = new Payment(new BigDecimal("100.00"), loan);
        payment2.setPaymentId(2L);
        payment2.setPaymentDateTime(new Date());

        when(loanRepository.findById(loanId)).thenReturn(java.util.Optional.of(loan));
        when(paymentRepository.findAllByLoanId(loanId)).thenReturn(List.of(payment1, payment2));

        //when
        LoanWithPaymentsDTO result = processingService.getLoanWithPayments(loanId);

        //then
        assertNotNull(result);
        assertEquals(2, result.getPayments().size());
        assertEquals(payment1.getPaymentAmount(), result.getPayments().get(0).getPaymentAmount());
        assertEquals(payment2.getPaymentAmount(), result.getPayments().get(1).getPaymentAmount());
    }
}
