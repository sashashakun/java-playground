package com.example.fintech.day3.patterns;

import java.math.BigDecimal;
import java.math.RoundingMode;

public interface PricingStrategy {

    BigDecimal calculateFee(BigDecimal amount);

    class StandardPricingStrategy implements PricingStrategy {
        private static final BigDecimal RATE = new BigDecimal("0.02");
        @Override
        public BigDecimal calculateFee(BigDecimal amount) {
            return amount.multiply(RATE).setScale(2, RoundingMode.HALF_EVEN);
        }
    }

    class VolumePricingStrategy implements PricingStrategy {
        private static final BigDecimal T1 = new BigDecimal("10000");
        private static final BigDecimal T2 = new BigDecimal("50000");
        @Override
        public BigDecimal calculateFee(BigDecimal amount) {
            BigDecimal rate;
            if (amount.compareTo(T1) < 0)       rate = new BigDecimal("0.02");
            else if (amount.compareTo(T2) < 0)  rate = new BigDecimal("0.015");
            else                                 rate = new BigDecimal("0.01");
            return amount.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
        }
    }

    class PromoPricingStrategy implements PricingStrategy {
        private final BigDecimal promoRate;

        public PromoPricingStrategy(BigDecimal promoRate) {
            this.promoRate = promoRate;
        }

        @Override
        public BigDecimal calculateFee(BigDecimal amount) {
            return amount.multiply(promoRate).setScale(2, RoundingMode.HALF_EVEN);
        }
    }
}
