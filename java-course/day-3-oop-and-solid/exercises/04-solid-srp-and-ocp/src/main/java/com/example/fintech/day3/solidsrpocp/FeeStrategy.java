package com.example.fintech.day3.solidsrpocp;

import java.math.BigDecimal;

/**
 * Exercise 04 — Open/Closed Principle via Strategy
 *
 * Each fee calculation strategy is a separate class.
 * Adding new transaction types means adding a new class — NOT editing existing ones.
 *
 * Provided — no changes needed.
 */
@FunctionalInterface
public interface FeeStrategy {

    /**
     * Calculate the fee for a given transaction amount.
     *
     * @param amount the gross transaction amount (always positive)
     * @return the fee amount (may be zero, never negative)
     */
    BigDecimal calculate(BigDecimal amount);
}
