package com.example.fintech.day3.composition;

import java.math.BigDecimal;

/**
 * Exercise 03 — Input record being validated.
 * Provided — no changes needed.
 */
public record PaymentRequest(String id, BigDecimal amount, String currency, String merchantId) {}
