package com.bancx.payment.controllers;

import com.bancx.application.services.ProcessingService;
import com.bancx.payment.data.PaymentInDTO;
import com.bancx.payment.data.PaymentOutDTO;
import com.bancx.payment.services.PaymentDomainService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
public class PaymentController {
    @Autowired
    private PaymentDomainService paymentDomainService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProcessingService processingService;

    public PaymentController(PaymentDomainService paymentDomainService, ModelMapper modelMapper, ProcessingService processingService) {
        this.paymentDomainService = paymentDomainService;
        this.modelMapper = modelMapper;
        this.processingService = processingService;
    }

    @GetMapping("/payments/{paymentId}")
    PaymentOutDTO getPaymentById(@PathVariable Long paymentId) {
        return paymentDomainService.getPaymentById(paymentId);
    }

    @PostMapping("/payments")
    @Transactional
    PaymentOutDTO newPayment(@RequestBody @Valid PaymentInDTO newPayment) {
        PaymentOutDTO payOutDTO = modelMapper.map(processingService.makePayment(newPayment), PaymentOutDTO.class);
        payOutDTO.setLoanId(newPayment.getLoanId());

        return payOutDTO;
    }
}
