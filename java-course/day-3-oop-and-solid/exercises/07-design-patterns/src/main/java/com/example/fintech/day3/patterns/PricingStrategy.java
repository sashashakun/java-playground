package com.example.fintech.day3.patterns;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Exercise 07 — Strategy Pattern: Payment Pricing
 *
 * Provided interface — no changes needed.
 *
 * Three concrete strategies to implement:
 *   StandardPricingStrategy  — flat 2% fee
 *   VolumePricingStrategy    — tiered: 2% up to 10k, 1.5% up to 50k, 1% above
 *   PromoPricingStrategy     — flat discounted rate (configurable)
 */
public interface PricingStrategy {

    BigDecimal calculateFee(BigDecimal amount);

    /**
     * TODO 1 — StandardPricingStrategy:
     *   fee = amount * 0.02, scaled 2dp HALF_EVEN
     */
    class StandardPricingStrategy implements PricingStrategy {
        @Override
        public BigDecimal calculateFee(BigDecimal amount) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    /**
     * TODO 2 — VolumePricingStrategy:
     *   amount < 10_000       → 2%
     *   amount < 50_000       → 1.5%
     *   amount >= 50_000      → 1%
     *   Result scaled 2dp HALF_EVEN
     */
    class VolumePricingStrategy implements PricingStrategy {
        @Override
        public BigDecimal calculateFee(BigDecimal amount) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    /**
     * TODO 3 — PromoPricingStrategy:
     *   fee = amount * promoRate, scaled 2dp HALF_EVEN
     *   promoRate is provided via constructor (e.g. 0.005 for 0.5%)
     */
    class PromoPricingStrategy implements PricingStrategy {
        private final BigDecimal promoRate;

        public PromoPricingStrategy(BigDecimal promoRate) {
            this.promoRate = promoRate;
        }

        @Override
        public BigDecimal calculateFee(BigDecimal amount) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }
}
