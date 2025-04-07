package com.bancx.loan.data;

import com.bancx.application.annotations.MaxTwoDecimalPlaces;
import com.bancx.loan.constants.Constants;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class LoanOutDTO {
    @NotNull(message = "Loan ID cannot be null")
    @Setter
    private Long loanId;

    @NotNull(message = "Loan amount cannot be null")
    @Min(value = 1, message = "Loan amount must be greater than 0")
    @MaxTwoDecimalPlaces
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
    @MaxTwoDecimalPlaces
    private BigDecimal loanRemainingBalance;

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public void setLoanRemainingBalance(BigDecimal loanRemainingBalance) {
        this.loanRemainingBalance = loanRemainingBalance.setScale(2, RoundingMode.HALF_UP);
    }
}
