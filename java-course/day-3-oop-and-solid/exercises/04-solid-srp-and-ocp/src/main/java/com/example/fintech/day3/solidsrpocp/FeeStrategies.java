package com.example.fintech.day3.solidsrpocp;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Exercise 04 — Concrete Fee Strategies
 *
 * TODO 1 — PurchaseFeeStrategy: 1.5% of amount, scaled to 2dp HALF_EVEN
 *
 * TODO 2 — TransferFeeStrategy: 0.5% of amount, scaled to 2dp HALF_EVEN
 *
 * TODO 3 — CryptoFeeStrategy: 0.5% of amount, minimum 0.50, scaled to 2dp HALF_EVEN
 *   fee = max(amount * 0.005, 0.50)
 *
 * TODO 4 — ZeroFeeStrategy: always returns BigDecimal.ZERO
 *
 * Hint: Each is a nested class implementing FeeStrategy.
 * Or you can use static factory lambdas — your choice.
 */
public final class FeeStrategies {

    private FeeStrategies() {}

    // TODO 1
    public static class PurchaseFeeStrategy implements FeeStrategy {
        @Override
        public BigDecimal calculate(BigDecimal amount) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    // TODO 2
    public static class TransferFeeStrategy implements FeeStrategy {
        @Override
        public BigDecimal calculate(BigDecimal amount) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    // TODO 3
    public static class CryptoFeeStrategy implements FeeStrategy {
        private static final BigDecimal MIN_FEE = new BigDecimal("0.50");
        private static final BigDecimal RATE    = new BigDecimal("0.005");

        @Override
        public BigDecimal calculate(BigDecimal amount) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    // TODO 4
    public static class ZeroFeeStrategy implements FeeStrategy {
        @Override
        public BigDecimal calculate(BigDecimal amount) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }
}
