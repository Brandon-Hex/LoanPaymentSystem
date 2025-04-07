package com.bancx.payment.data;

import com.bancx.application.annotations.MaxTwoDecimalPlaces;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class PaymentOutDTO {
    @NotNull(message = "Loan ID cannot be null")
    private Long paymentId;

    @NotNull(message = "Loan ID cannot be null")
    private Long loanId;

    @NotNull(message = "Payment amount cannot be null")
    @Min(value = 0, message = "Payment amount cannot be less than 0")
    @MaxTwoDecimalPlaces
    private BigDecimal paymentAmount;

    @NotNull(message = "Payment Date cannot be null")
    private Date paymentDate;
}
