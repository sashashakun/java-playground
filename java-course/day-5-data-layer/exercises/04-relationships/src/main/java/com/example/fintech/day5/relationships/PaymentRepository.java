package com.example.fintech.day5.relationships;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByMerchant(Merchant merchant);
}
