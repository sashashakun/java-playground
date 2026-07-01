package com.example.fintech.capstone.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Capstone Exercise H — Service: FxService
 *
 * FX rates change infrequently — caching prevents hammering the external provider.
 * The provider call can fail transiently — retry before giving up.
 *
 * TODO H1: Add @Cacheable(value = "fx-rates", key = "#currency") to getRate.
 *          This caches the result so repeat calls for the same currency are instant.
 *
 * TODO H2: Add @CacheEvict(value = "fx-rates", allEntries = true) to refreshAll.
 *          This clears the cache so next getRate calls re-fetch from the provider.
 *
 * TODO H3: Add @Retryable(retryFor = FxProviderException.class, maxAttempts = 3,
 *                         backoff = @Backoff(delay = 200)) to fetchFromProvider.
 *          This retries up to 3 times with 200ms delay on transient failures.
 */
@Service
public class FxService {

    // Simulated "external provider" call count for tests
    private int providerCallCount = 0;
    private final Map<String, BigDecimal> rates = new ConcurrentHashMap<>(Map.of(
        "USD", BigDecimal.ONE,
        "EUR", new BigDecimal("0.92"),
        "GBP", new BigDecimal("0.79"),
        "JPY", new BigDecimal("149.50")
    ));

    // TODO H1: @Cacheable(value = "fx-rates", key = "#currency")
    public BigDecimal getRate(String currency) {
        return fetchFromProvider(currency);
    }

    // TODO H2: @CacheEvict(value = "fx-rates", allEntries = true)
    public void refreshAll() {
        // triggers re-fetch on next getRate call
    }

    // TODO H3: @Retryable(retryFor = FxProviderException.class, maxAttempts = 3, backoff = @Backoff(delay = 200))
    public BigDecimal fetchFromProvider(String currency) {
        providerCallCount++;
        BigDecimal rate = rates.get(currency.toUpperCase());
        if (rate == null) {
            throw new FxProviderException("Unknown currency: " + currency);
        }
        return rate;
    }

    public int getProviderCallCount() { return providerCallCount; }
    public void resetCallCount() { providerCallCount = 0; }

    public static class FxProviderException extends RuntimeException {
        public FxProviderException(String msg) { super(msg); }
    }
}
