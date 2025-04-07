package com.bancx.application.data;

import com.bancx.loan.constants.Constants;
import com.bancx.payment.data.PaymentOutDTO;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class LoanWithPaymentsDTO {
    @NotNull(message = "Loan amount cannot be null")
    @Min(value = 1, message = "Loan amount must be greater than 0")
    private BigDecimal loanAmount;

    @NotNull(message = "Loan Term cannot be null")
    @Min(value = 1, message = "Loan Term must be greater than 0")
    private int term;

    @Enumerated(EnumType.STRING)
    private Constants.LoanStatus status;

    @NotNull(message = "Loan balance cannot be null")
    @Min(value = 0, message = "Loan balance cannot be less than 0")
    private BigDecimal loanRemainingBalance;

    private List<PaymentOutDTO> payments;
}
