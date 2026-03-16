package com.example.fintech.day3.soliddip;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Exercise 06 — Domain object. Provided — no changes needed.
 */
public record Payment(String id, BigDecimal amount, String currency,
                      String status, Instant createdAt) {

    public Payment withStatus(String newStatus) {
        return new Payment(id, amount, currency, newStatus, createdAt);
    }
}
