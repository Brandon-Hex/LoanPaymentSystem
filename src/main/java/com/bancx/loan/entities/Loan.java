package com.bancx.loan.entities;

import com.bancx.application.annotations.MaxTwoDecimalPlaces;
import com.bancx.loan.constants.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name="LOAN")
@Getter
@Setter
@NoArgsConstructor
public class Loan {
    @Id
    @GeneratedValue
    private Long loanId;

    @NotNull(message = "Loan amount cannot be null")
    @Min(value = 1, message = "Loan amount must be greater than 0")
    @MaxTwoDecimalPlaces
    private BigDecimal initialLoanAmount;

    @NotNull(message = "Loan balance cannot be null")
    @Min(value = 0, message = "Loan balance cannot be less than 0")
    @MaxTwoDecimalPlaces
    private BigDecimal loanRemainingBalance;

    @NotNull(message = "Loan Term cannot be null")
    @Min(value = 1, message = "Loan Term must be greater than 0")
    private int term;

    @Enumerated(EnumType.STRING)
    private Constants.LoanStatus status;

    public Loan(BigDecimal initialLoanAmount, int term) {
        BigDecimal loanAmountToTwoDecimals = initialLoanAmount.setScale(2, RoundingMode.HALF_UP);
        this.setInitialLoanAmount(loanAmountToTwoDecimals);
        this.setTerm(term);
        this.setStatus(Constants.LoanStatus.ACTIVE);
        this.loanRemainingBalance = loanAmountToTwoDecimals;
    }
}
