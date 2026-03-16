package com.example.fintech.day5.pagination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    Page<Payment> findByStatus(PaymentStatus status, Pageable pageable);

    Slice<Payment> findByMerchantId(String merchantId, Pageable pageable);

    @Query(value = "SELECT p FROM Payment p WHERE p.amount.value > :threshold ORDER BY p.amount.value DESC",
           countQuery = "SELECT count(p) FROM Payment p WHERE p.amount.value > :threshold")
    Page<Payment> findHighValuePayments(@Param("threshold") BigDecimal threshold, Pageable pageable);
}
