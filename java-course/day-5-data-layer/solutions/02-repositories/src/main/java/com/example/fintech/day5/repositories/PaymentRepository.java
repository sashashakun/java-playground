package com.example.fintech.day5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByMerchantId(String merchantId);

    List<Payment> findByAmount_ValueGreaterThan(BigDecimal threshold);

    boolean existsByIdempotencyKey(String idempotencyKey);

    List<Payment> findByMerchantIdOrderByCreatedAtDesc(String merchantId);
}
