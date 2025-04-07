package com.bancx.loan.data;

import com.bancx.application.annotations.MaxTwoDecimalPlaces;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class LoanInDTO {
    @NotNull(message = "Loan amount cannot be null")
    @Min(value = 1, message = "Loan amount must be greater than 0")
    @MaxTwoDecimalPlaces
    private BigDecimal loanAmount;

    @NotNull(message = "Loan Term cannot be null")
    @Min(value = 1, message = "Loan Term must be greater than 0")
    @Setter
    private int term;

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount.setScale(2, RoundingMode.HALF_UP);
    }
}
