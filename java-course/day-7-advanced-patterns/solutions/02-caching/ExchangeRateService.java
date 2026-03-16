package com.example.fintech.day7.caching;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

// SOLUTION 02 — Caching

@Service
public class ExchangeRateService {

    private final ExchangeRateProvider provider;

    public ExchangeRateService(ExchangeRateProvider provider) {
        this.provider = provider;
    }

    @Cacheable(value = "rates", key = "#currency")          // TODO 1 ✓
    public BigDecimal getRate(String currency) {
        return provider.fetchRate(currency);
    }

    @CachePut(value = "rates", key = "#currency")           // TODO 2 ✓
    public BigDecimal updateRate(String currency, BigDecimal newRate) {
        return newRate;
    }

    @CacheEvict(value = "rates", allEntries = true)         // TODO 3 ✓
    public void evictAll() {
    }

    @CacheEvict(value = "rates", key = "#currency")         // TODO 4 ✓
    public void evictCurrency(String currency) {
    }
}
