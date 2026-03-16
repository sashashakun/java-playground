package com.example.fintech.day1.records;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Solution for Exercise 06 — Money record */
public record Money(BigDecimal amount, String currency) {

    public Money {
        Objects.requireNonNull(amount, "amount must not be null");
        if (currency == null || currency.isBlank())
            throw new IllegalArgumentException("currency must not be blank");
        if (!currency.matches("[A-Z]{3}"))
            throw new IllegalArgumentException("invalid currency code: " + currency);
        // Normalize scale — negative amounts are allowed (represent overdrafts/debits)
        amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public Money add(Money other) {
        requireSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        requireSameCurrency(other);
        // Subtraction may produce a negative result (overdraft is valid in accounting)
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public String formatted() {
        return "%s %s".formatted(currency, amount.toPlainString());
    }

    private void requireSameCurrency(Money other) {
        if (!this.currency.equals(other.currency))
            throw new IllegalArgumentException(
                "Cannot operate on %s and %s".formatted(this.currency, other.currency));
    }
}
