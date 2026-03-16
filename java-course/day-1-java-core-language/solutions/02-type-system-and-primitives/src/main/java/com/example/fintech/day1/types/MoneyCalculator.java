package com.example.fintech.day1.types;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/** Solution for Exercise 02 — Type System & Primitives */
public class MoneyCalculator {

    public BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }

    public BigDecimal convertCurrency(BigDecimal amount, BigDecimal rate, int scale) {
        return amount.multiply(rate).setScale(scale, RoundingMode.HALF_EVEN);
    }

    public BigDecimal calculateFee(BigDecimal amount, BigDecimal ratePercent) {
        return amount
            .multiply(ratePercent)
            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_EVEN);
    }

    public List<BigDecimal> splitAmount(BigDecimal total, int parts) {
        if (parts <= 0) throw new IllegalArgumentException("parts must be positive, was: " + parts);

        List<BigDecimal> result = new ArrayList<>(parts);
        BigDecimal baseShare = total.divide(BigDecimal.valueOf(parts), 2, RoundingMode.DOWN);

        for (int i = 0; i < parts - 1; i++) {
            result.add(baseShare);
        }

        // Last part absorbs rounding remainder
        BigDecimal remainder = total.subtract(baseShare.multiply(BigDecimal.valueOf(parts - 1)));
        result.add(remainder.setScale(2, RoundingMode.UNNECESSARY));

        return result;
    }

    public boolean isWithinLimit(long amountCents, long limitCents) {
        return amountCents <= limitCents;
    }
}
