package com.bancx.loan;

import com.bancx.loan.constants.Constants;
import com.bancx.loan.data.LoanInDTO;
import com.bancx.loan.entities.Loan;
import com.bancx.loan.exceptions.LoanNotFoundException;
import com.bancx.loan.repositories.LoanRepository;
import com.bancx.loan.services.LoanDomainService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;


public class LoanTests {
    Loan newLoan;
    LoanInDTO newLoanInDTO;

    private Validator validator;

    private LoanDomainService loanDomainService;
    private LoanRepository loanRepository;

    @BeforeEach
    public void setup() {
        loanRepository = mock(LoanRepository.class);
        loanDomainService = new LoanDomainService(loanRepository);

        validator = Validation.buildDefaultValidatorFactory().getValidator();

        Loan newLoan = new Loan(new BigDecimal("100.29"), 5);
        newLoan.setLoanId(1L);
        this.newLoan = newLoan;

        LoanInDTO newLoanInDTO = new LoanInDTO();
        newLoanInDTO.setLoanAmount(new BigDecimal("100.29"));
        newLoanInDTO.setTerm(5);
        this.newLoanInDTO = newLoanInDTO;
    }

    @Test
    void testSaveLoan_ifIsValid() {
        //given
        when(loanRepository.save(any(Loan.class))).thenReturn(newLoan);

        //when
        Loan createdLoan = loanDomainService.saveNewLoan(newLoanInDTO);

        //then
        assertNotNull(createdLoan);
        assertEquals(Constants.LoanStatus.ACTIVE, createdLoan.getStatus());
    }

    @Test
    void testSaveLoan_shouldThrowException_whenAmountInvalid() {
        //given
        newLoan.setInitialLoanAmount(new BigDecimal("0"));
        when(loanRepository.save(any(Loan.class))).thenReturn(newLoan);

        //then
        Set<ConstraintViolation<Loan>> violations = validator.validate(newLoan);
        assertFalse(violations.isEmpty());
        assertEquals("Loan amount must be greater than 0", violations.iterator().next().getMessage());
    }

    @Test
    void testSaveLoan_shouldThrowException_whenTermInvalid() {
        //given
        newLoan.setTerm(0);
        when(loanRepository.save(any(Loan.class))).thenReturn(newLoan);

        //then
        Set<ConstraintViolation<Loan>> violations = validator.validate(newLoan);
        assertFalse(violations.isEmpty());
        assertEquals("Loan Term must be greater than 0", violations.iterator().next().getMessage());
    }

    @Test
    public void testGetLoanById_found() {
        //given
        Loan loan = new Loan(BigDecimal.valueOf(2000), 24);
        loan.setLoanId(2L);

        //when
        when(loanRepository.findById(2L)).thenReturn(Optional.of(loan));
        Loan result = loanDomainService.getLoanById(2L);

        //then
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(2000), result.getInitialLoanAmount());
        assertEquals(24, result.getTerm());
    }

    @Test
    public void testGetLoanById_shouldThrowException_whenNotFound() {
        //given
        when(loanRepository.findById(99L)).thenReturn(Optional.empty());

        //then
        assertThrows(LoanNotFoundException.class, () -> loanDomainService.getLoanById(99L));
    }

}




























