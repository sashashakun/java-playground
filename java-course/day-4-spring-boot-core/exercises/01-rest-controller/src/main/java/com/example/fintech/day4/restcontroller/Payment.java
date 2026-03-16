package com.example.fintech.day4.restcontroller;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Provided — no changes needed.
 */
public record Payment(
    String id,
    BigDecimal amount,
    String currency,
    String description,
    String status,
    Instant createdAt
) {}
