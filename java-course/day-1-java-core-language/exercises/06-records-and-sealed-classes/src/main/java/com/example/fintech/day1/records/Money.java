package com.example.fintech.day1.records;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Exercise 06 — Records & Sealed Classes (Part 1 of 3)
 *
 * An immutable monetary value. Implement the compact constructor
 * (validation) and the 4 arithmetic/display methods.
 *
 * Record syntax reminder:
 *   public record Money(BigDecimal amount, String currency) {
 *       // Compact constructor — runs BEFORE field assignment
 *       public Money {
 *           // validate here; throw if invalid
 *           // DO NOT manually assign — the compiler does it after this block
 *       }
 *       // Additional methods go here
 *   }
 *
 * Records automatically get:
 *   - Canonical constructor: new Money(amount, currency)
 *   - Accessors: money.amount(), money.currency()   ← no "get" prefix!
 *   - equals(), hashCode(), toString()
 */
public record Money(BigDecimal amount, String currency) {

    /**
     * TODO 1 — Compact constructor: validate the fields.
     *
     * Rules:
     *   - amount must not be null (throw NullPointerException with message "amount must not be null")
     *   - currency must not be null or blank (throw IllegalArgumentException "currency must not be blank")
     *   - currency must be exactly 3 uppercase ASCII letters (throw IllegalArgumentException
     *     "invalid currency code: " + currency)
     *   - Normalize amount: set scale to 2 with HALF_EVEN rounding
     *     (so Money("10.5", "USD") stores "10.50")
     *
     * Note: Money CAN represent negative values — negative Money represents a debit
     * or overdraft. The isPositive() method is used to check sign.
     *
     * Java hints:
     *   Objects.requireNonNull(amount, "amount must not be null");
     *   amount = amount.setScale(2, RoundingMode.HALF_EVEN);  ← reassign in compact constructor
     *   currency.matches("[A-Z]{3}")  ← regex for 3 uppercase letters
     */
    public Money {
        // TODO: implement validation (null checks + currency format + scale normalization)
        // Hint: validate amount not null, validate currency not blank and matches [A-Z]{3},
        //       then normalize amount scale to 2 decimal places with HALF_EVEN rounding
    }

    /**
     * TODO 2 — Add another Money amount to this one.
     *
     * Rules:
     *   - Both must have the same currency; throw IllegalArgumentException
     *     "Cannot add USD and EUR" if they differ
     *   - Return a new Money with the sum (records are immutable)
     *
     * Example:
     *   new Money("10.50", "USD").add(new Money("5.75", "USD"))
     *   → Money(16.25, "USD")
     */
    public Money add(Money other) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Subtract another Money from this one.
     *
     * Rules:
     *   - Same currency required (same error message as add)
     *   - Result CAN be negative (we allow negative balances — overdraft)
     *
     * Example:
     *   new Money("10.50", "USD").subtract(new Money("15.00", "USD"))
     *   → Money(-4.50, "USD")
     */
    public Money subtract(Money other) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 4 — Returns true if amount > 0.
     *
     * Example:
     *   new Money("10.00", "USD").isPositive() → true
     *   new Money("0.00",  "USD").isPositive() → false
     *   new Money("-1.00", "USD").isPositive() → false
     */
    public boolean isPositive() {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 5 — Return a display string.
     *
     * Format: "CURRENCY AMOUNT"
     *
     * Examples:
     *   new Money("15.99", "USD").formatted() → "USD 15.99"
     *   new Money("0.00",  "EUR").formatted() → "EUR 0.00"
     *
     * Note: Java record accessor for 'amount' is money.amount() not money.getAmount().
     * The field is already normalized to 2 decimal places in the constructor.
     * Use amount.toPlainString() to avoid scientific notation.
     */
    public String formatted() {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
