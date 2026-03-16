package com.example.fintech.day6.assertj;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Exercise 05 — AssertJ Advanced
 *
 * Domain model. Provided in full — not modified in this exercise.
 */
public record Transaction(
    String id,
    String userId,
    BigDecimal amount,
    String currency,
    TransactionType type,
    TransactionStatus status,
    String reference,
    Instant timestamp
) {
    public enum TransactionType { CREDIT, DEBIT, TRANSFER }
    public enum TransactionStatus { PENDING, COMPLETED, FAILED, REVERSED }
}
