package com.example.fintech.day4.profiles;

import java.math.BigDecimal;

/**
 * Exercise 07 — Gateway interface (Provided — no changes needed)
 */
public interface PaymentGateway {

    record ChargeResult(String transactionId, boolean success, String message) {}

    ChargeResult charge(String paymentId, BigDecimal amount, String currency);

    String getGatewayName();
}
