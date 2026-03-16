package com.example.fintech.day1.types;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Exercise 02 — Type System & Primitives
 *
 * Implement precise financial calculations using BigDecimal.
 *
 * The golden rules of financial arithmetic in Java:
 *   1. NEVER use double for money — use BigDecimal
 *   2. ALWAYS construct BigDecimal from String: new BigDecimal("0.1")
 *   3. ALWAYS specify a RoundingMode when dividing or setting scale
 *   4. Use RoundingMode.HALF_EVEN (banker's rounding) for financial calculations
 *
 * Quick BigDecimal reference:
 *   a.add(b)                           → a + b
 *   a.subtract(b)                      → a - b
 *   a.multiply(b)                      → a * b
 *   a.divide(b, 2, RoundingMode.X)     → a / b, rounded to 2 decimal places
 *   a.setScale(2, RoundingMode.X)      → round to 2 decimal places
 *   a.compareTo(b)                     → negative/zero/positive (like a - b)
 *   a.compareTo(BigDecimal.ZERO) > 0   → a > 0
 */
public class MoneyCalculator {

    /**
     * TODO 1 — Add two BigDecimal amounts.
     *
     * No rounding needed here — addition is exact.
     *
     * Example:
     *   add(new BigDecimal("10.50"), new BigDecimal("0.10")) → 10.60
     */
    public BigDecimal add(BigDecimal a, BigDecimal b) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Convert an amount from one currency to another.
     *
     * Formula: result = amount × rate, rounded to 'scale' decimal places
     * using HALF_EVEN rounding.
     *
     * Examples:
     *   convertCurrency("100.00", "1.0847", 2) → 108.47  (USD → EUR at 1.0847)
     *   convertCurrency("1.00",   "0.1",    8) → 0.10000000
     *
     * @param amount  the source amount
     * @param rate    the exchange rate (target per source unit)
     * @param scale   decimal places in the result
     */
    public BigDecimal convertCurrency(BigDecimal amount, BigDecimal rate, int scale) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Calculate a percentage fee on an amount.
     *
     * Formula: fee = amount × (ratePercent / 100), rounded to 2 decimal places
     * using HALF_EVEN rounding.
     *
     * Examples:
     *   calculateFee("100.00", "1.5")   → 1.50   (1.5% of 100.00)
     *   calculateFee("33.33",  "10.0")  → 3.33   (10% of 33.33)
     *   calculateFee("0.01",   "50.0")  → 0.01   (50% of 0.01, rounded up from 0.005)
     *
     * Note on 0.005: HALF_EVEN rounds 0.005 → 0.00 (rounds to even = 0),
     * but 0.01 × 50% = 0.005, and HALF_EVEN applied to 3 decimal places
     * gives 0.00, then scale to 2 gives 0.00. Check the test for expected value.
     */
    public BigDecimal calculateFee(BigDecimal amount, BigDecimal ratePercent) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 4 — Split an amount into N equal parts.
     *
     * The last part absorbs any rounding remainder so the parts always sum to total.
     *
     * Algorithm:
     *   1. Divide total by parts, round DOWN to 2 decimal places (each "base" share)
     *   2. Calculate remainder = total - (baseShare × (parts - 1))
     *   3. Return list of (parts - 1) copies of baseShare, then the remainder
     *
     * Examples:
     *   splitAmount("10.00", 3) → [3.33, 3.33, 3.34]   (3.33 + 3.33 + 3.34 = 10.00)
     *   splitAmount("1.00",  2) → [0.50, 0.50]
     *   splitAmount("0.01",  3) → [0.00, 0.00, 0.01]
     *
     * Edge case: parts must be > 0, else throw IllegalArgumentException.
     */
    public List<BigDecimal> splitAmount(BigDecimal total, int parts) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 5 — Check if an amount in cents is within a limit.
     *
     * Returns true if amountCents <= limitCents.
     * Both values are in minor units (e.g., USD cents, EUR cents).
     *
     * This uses primitive long comparison — notice how this is different from
     * BigDecimal.compareTo(). For simple integer comparisons, primitives are fine.
     *
     * Examples:
     *   isWithinLimit(1000L, 5000L) → true   ($10.00 is within $50.00 limit)
     *   isWithinLimit(5000L, 5000L) → true   (exactly at limit)
     *   isWithinLimit(5001L, 5000L) → false  (exceeds limit by 1 cent)
     */
    public boolean isWithinLimit(long amountCents, long limitCents) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
