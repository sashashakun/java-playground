package com.example.fintech.day6.assertj;

import java.math.BigDecimal;
import java.util.List;

/**
 * Exercise 05 — AssertJ Advanced
 *
 * A ledger report aggregates transactions for a user.
 * Provided in full.
 */
public record LedgerReport(
    String userId,
    List<Transaction> transactions,
    BigDecimal totalCredits,
    BigDecimal totalDebits,
    BigDecimal netBalance,
    String reportCurrency
) {
    public static LedgerReport empty(String userId) {
        return new LedgerReport(userId, List.of(),
            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "USD");
    }
}
