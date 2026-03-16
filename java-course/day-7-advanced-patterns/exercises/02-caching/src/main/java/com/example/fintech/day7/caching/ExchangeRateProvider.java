package com.example.fintech.day7.caching;

import java.math.BigDecimal;

/**
 * Exercise 02 — Caching
 *
 * Simulates an external HTTP provider (expensive call, should be cached).
 * In tests this will be replaced with a mock that counts invocations.
 */
public interface ExchangeRateProvider {
    /** @return USD value of 1 unit of the given currency (e.g. "EUR" → 1.08) */
    BigDecimal fetchRate(String currency);
}
