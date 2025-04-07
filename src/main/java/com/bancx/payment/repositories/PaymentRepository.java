package com.bancx.payment.repositories;

import com.bancx.payment.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT p FROM Payment p WHERE p.loan.loanId = :loanId")
    public List<Payment> findAllByLoanId(@Param("loanId") Long loanId);
}
