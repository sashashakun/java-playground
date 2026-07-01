package com.example.fintech.capstone.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// SOLUTION H — Service: FxService

@Service
public class FxService {

    private int providerCallCount = 0;
    private final Map<String, BigDecimal> rates = new ConcurrentHashMap<>(Map.of(
        "USD", BigDecimal.ONE,
        "EUR", new BigDecimal("0.92"),
        "GBP", new BigDecimal("0.79"),
        "JPY", new BigDecimal("149.50")
    ));

    @Cacheable(value = "fx-rates", key = "#currency")  // TODO H1 ✓
    public BigDecimal getRate(String currency) {
        return fetchFromProvider(currency);
    }

    @CacheEvict(value = "fx-rates", allEntries = true)  // TODO H2 ✓
    public void refreshAll() {}

    @Retryable(                                          // TODO H3 ✓
        retryFor = FxProviderException.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 200)
    )
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
