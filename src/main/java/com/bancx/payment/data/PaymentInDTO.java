package com.bancx.payment.data;

import com.bancx.application.annotations.MaxTwoDecimalPlaces;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class PaymentInDTO {
    @NotNull(message = "Loan ID cannot be null")
    @Setter
    private Long loanId;

    @NotNull(message = "Payment amount cannot be null")
    @Min(value = 0, message = "Payment amount cannot be less than 0")
    @MaxTwoDecimalPlaces
    private BigDecimal paymentAmount;

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount.setScale(2, RoundingMode.HALF_UP);
    }
}