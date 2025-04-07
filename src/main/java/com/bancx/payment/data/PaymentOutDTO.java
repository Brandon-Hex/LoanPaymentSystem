package com.bancx.payment.data;

import com.bancx.application.annotations.MaxTwoDecimalPlaces;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Getter
public class PaymentOutDTO {
    @NotNull(message = "Loan ID cannot be null")
    @Setter
    private Long paymentId;

    @NotNull(message = "Loan ID cannot be null")
    @Setter
    private Long loanId;

    @NotNull(message = "Payment amount cannot be null")
    @Min(value = 0, message = "Payment amount cannot be less than 0")
    @MaxTwoDecimalPlaces
    private BigDecimal paymentAmount;

    @NotNull(message = "Payment Date cannot be null")
    @Setter
    private Date paymentDate;

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount.setScale(2, RoundingMode.HALF_UP);
    }
}
