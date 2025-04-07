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
import java.math.RoundingMode;
import java.util.List;

@Getter
public class LoanWithPaymentsDTO {
    @NotNull(message = "Loan amount cannot be null")
    @Min(value = 1, message = "Loan amount must be greater than 0")
    private BigDecimal loanAmount;

    @NotNull(message = "Loan Term cannot be null")
    @Min(value = 1, message = "Loan Term must be greater than 0")
    @Setter
    private int term;

    @Enumerated(EnumType.STRING)
    @Setter
    private Constants.LoanStatus status;

    @NotNull(message = "Loan balance cannot be null")
    @Min(value = 0, message = "Loan balance cannot be less than 0")
    private BigDecimal loanRemainingBalance;

    @Setter
    private List<PaymentOutDTO> payments;

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public void setLoanRemainingBalance(BigDecimal loanRemainingBalance) {
        this.loanRemainingBalance = loanRemainingBalance.setScale(2, RoundingMode.HALF_UP);
    }
}
