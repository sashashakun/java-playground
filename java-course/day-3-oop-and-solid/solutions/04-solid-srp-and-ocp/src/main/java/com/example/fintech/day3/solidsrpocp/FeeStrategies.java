package com.example.fintech.day3.solidsrpocp;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class FeeStrategies {

    private FeeStrategies() {}

    public static class PurchaseFeeStrategy implements FeeStrategy {
        private static final BigDecimal RATE = new BigDecimal("0.015");
        @Override
        public BigDecimal calculate(BigDecimal amount) {
            return amount.multiply(RATE).setScale(2, RoundingMode.HALF_EVEN);
        }
    }

    public static class TransferFeeStrategy implements FeeStrategy {
        private static final BigDecimal RATE = new BigDecimal("0.005");
        @Override
        public BigDecimal calculate(BigDecimal amount) {
            return amount.multiply(RATE).setScale(2, RoundingMode.HALF_EVEN);
        }
    }

    public static class CryptoFeeStrategy implements FeeStrategy {
        private static final BigDecimal MIN_FEE = new BigDecimal("0.50");
        private static final BigDecimal RATE    = new BigDecimal("0.005");
        @Override
        public BigDecimal calculate(BigDecimal amount) {
            BigDecimal fee = amount.multiply(RATE).setScale(2, RoundingMode.HALF_EVEN);
            return fee.max(MIN_FEE);
        }
    }

    public static class ZeroFeeStrategy implements FeeStrategy {
        @Override
        public BigDecimal calculate(BigDecimal amount) {
            return BigDecimal.ZERO;
        }
    }
}
