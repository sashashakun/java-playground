package com.example.fintech.day5.transactions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByMerchantId(String merchantId);
    boolean existsByIdempotencyKey(String key);
}
