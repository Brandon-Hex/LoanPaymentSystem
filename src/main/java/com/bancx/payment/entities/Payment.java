package com.bancx.payment.entities;

import com.bancx.application.annotations.MaxTwoDecimalPlaces;
import com.bancx.loan.entities.Loan;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "PAYMENT")
@Getter
@Setter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name="loanId", nullable=false)
    @NotNull(message = "Loan ID cannot be null")
    private Loan loan;

    @NotNull(message = "Payment amount cannot be null")
    @Min(value = 0, message = "Payment amount cannot be less than 0")
    @MaxTwoDecimalPlaces
    private BigDecimal paymentAmount;

    @NotNull(message = "Payment Date cannot be null")
    private Date paymentDateTime;

    public Payment(BigDecimal paymentAmount, Loan loan) {
        this.paymentAmount = paymentAmount;
        this.loan = loan;
        this.paymentDateTime = new Date();
    }
}
