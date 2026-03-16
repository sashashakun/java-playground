package com.example.fintech.day2.concurrencyfoundations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;

class ExchangeRateServiceTest {

    private ExchangeRateService service;

    @BeforeEach
    void setUp() {
        // Instant fetcher: EUR=1.10, USD=1.00, anything else throws
        ExchangeRateService.RateFetcher fetcher = currency -> switch (currency) {
            case "EUR" -> new BigDecimal("1.10");
            case "USD" -> BigDecimal.ONE;
            default -> throw new RuntimeException("Unknown currency: " + currency);
        };
        service = new ExchangeRateService(fetcher, Executors.newSingleThreadExecutor());
    }

    @Test
    void getRateFetchesSuccessfully() throws Exception {
        BigDecimal rate = service.getRate("EUR").get();
        assertThat(rate).isEqualByComparingTo("1.10");
    }

    @Test
    void getRateCachesOnSecondCall() throws Exception {
        service.getRate("EUR").get();
        assertThat(service.isCached("EUR")).isTrue();
        long countBefore = service.getRequestCount();
        service.getRate("EUR").get(); // should hit cache
        assertThat(service.getRequestCount()).isEqualTo(countBefore); // no new request
    }

    @Test
    void getRateIncrementsRequestCount() throws Exception {
        service.getRate("EUR").get();
        assertThat(service.getRequestCount()).isEqualTo(1L);
    }

    @Test
    void getRatesReturnsBothCurrencies() throws Exception {
        Map<String, BigDecimal> rates = service.getRates(Set.of("EUR", "USD")).get();
        assertThat(rates).containsKey("EUR").containsKey("USD");
        assertThat(rates.get("EUR")).isEqualByComparingTo("1.10");
    }

    @Test
    void getRateOrDefaultReturnsFallbackOnError() throws Exception {
        BigDecimal fallback = new BigDecimal("999");
        BigDecimal result = service.getRateOrDefault("XYZ", fallback).get();
        assertThat(result).isEqualByComparingTo("999");
    }

    @Test
    void getRateOrDefaultReturnsRateOnSuccess() throws Exception {
        BigDecimal result = service.getRateOrDefault("USD", new BigDecimal("0")).get();
        assertThat(result).isEqualByComparingTo("1.00");
    }

    @Test
    void clearCacheReturnsCountAndClearsCache() throws Exception {
        service.getRate("EUR").get();
        service.getRate("USD").get();
        int removed = service.clearCache();
        assertThat(removed).isEqualTo(2);
        assertThat(service.isCached("EUR")).isFalse();
        assertThat(service.isCached("USD")).isFalse();
    }

    @Test
    void clearCacheReturnsZeroWhenEmpty() {
        assertThat(service.clearCache()).isEqualTo(0);
    }
}
