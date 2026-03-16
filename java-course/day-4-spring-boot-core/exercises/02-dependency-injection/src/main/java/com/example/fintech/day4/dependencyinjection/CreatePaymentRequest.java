package com.example.fintech.day4.dependencyinjection;

import java.math.BigDecimal;

/**
 * Provided — no changes needed.
 * (Validation annotations come in Exercise 03.)
 */
public record CreatePaymentRequest(
    BigDecimal amount,
    String currency,
    String description
) {}
