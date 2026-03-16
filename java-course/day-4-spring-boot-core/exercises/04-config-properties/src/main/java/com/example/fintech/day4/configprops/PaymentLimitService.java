package com.example.fintech.day4.configprops;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Exercise 04 — Service that uses PaymentConfig
 *
 * Demonstrates constructor injection of @ConfigurationProperties.
 *
 * TODO 1 — Add @Service annotation
 *
 * TODO 2 — Implement isWithinLimit(BigDecimal amount):
 *   Return true if amount <= config.getMaxAmount()
 *   Return false otherwise
 *
 * TODO 3 — Implement isSupportedCurrency(String currency):
 *   Return true if config.getSupportedCurrencies() contains currency
 *
 * TODO 4 — Implement calculateFee(BigDecimal amount):
 *   Return amount.multiply(config.getDefaultFeeRate())
 *   scaled to 2 decimal places HALF_EVEN
 */
// TODO 1: @Service
public class PaymentLimitService {

    private final PaymentConfig config;

    public PaymentLimitService(PaymentConfig config) {
        this.config = config;
    }

    public boolean isWithinLimit(BigDecimal amount) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public boolean isSupportedCurrency(String currency) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public BigDecimal calculateFee(BigDecimal amount) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
