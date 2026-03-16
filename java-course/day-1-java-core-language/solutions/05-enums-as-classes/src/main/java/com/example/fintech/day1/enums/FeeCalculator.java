package com.example.fintech.day1.enums;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Solution for Exercise 05 — FeeCalculator */
public class FeeCalculator {

    private static final BigDecimal CRYPTO_RATE   = new BigDecimal("0.005");
    private static final BigDecimal PURCHASE_RATE = new BigDecimal("0.015");
    private static final long CRYPTO_MIN_CENTS    = 50L;
    private static final long TRANSFER_FEE_CENTS  = 25L;

    public long calculateFee(long amountCents, TransactionType transactionType, Currency currency) {
        if (transactionType == TransactionType.FEE) return 0L;

        if (currency.isCrypto()) {
            long fee = BigDecimal.valueOf(amountCents)
                .multiply(CRYPTO_RATE)
                .setScale(0, RoundingMode.HALF_EVEN)
                .longValue();
            return Math.max(fee, CRYPTO_MIN_CENTS);
        }

        return switch (transactionType) {
            case PURCHASE -> BigDecimal.valueOf(amountCents)
                .multiply(PURCHASE_RATE)
                .setScale(0, RoundingMode.HALF_EVEN)
                .longValue();
            case TRANSFER_OUT -> TRANSFER_FEE_CENTS;
            default -> 0L;
        };
    }

    public boolean hasFee(long amountCents, TransactionType transactionType, Currency currency) {
        return calculateFee(amountCents, transactionType, currency) > 0;
    }
}
