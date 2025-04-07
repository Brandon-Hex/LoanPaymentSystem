package com.bancx.loan.controllers;

import com.bancx.application.data.LoanWithPaymentsDTO;
import com.bancx.application.services.ProcessingService;
import com.bancx.loan.data.LoanInDTO;
import com.bancx.loan.data.LoanOutDTO;
import com.bancx.loan.services.LoanDomainService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoanController {

    @Autowired
    private final LoanDomainService loanDomainService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProcessingService processingService;

    public LoanController(LoanDomainService loanDomainService, ModelMapper modelMapper, ProcessingService processingService) {
        this.loanDomainService = loanDomainService;
        this.modelMapper = modelMapper;
        this.processingService = processingService;
    }

    @GetMapping("/loans/{loanId}")
    LoanOutDTO getLoanById(@PathVariable Long loanId) {
        return modelMapper.map(loanDomainService.getLoanById(loanId), LoanOutDTO.class);
    }

    @GetMapping("/loans/include_payments/{loanId}")
    LoanWithPaymentsDTO getLoanWithPaymentsById(@PathVariable Long loanId) {
        return processingService.getLoanWithPayments(loanId);
    }


    @PostMapping("/loans")
    @Transactional
    LoanOutDTO newLoan(@RequestBody @Valid LoanInDTO newLoan) {
        return modelMapper.map(loanDomainService.saveNewLoan(newLoan), LoanOutDTO.class);
    }
}
