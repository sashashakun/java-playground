package com.example.fintech.day7.caching;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Exercise 02 — Caching
 *
 * Exchange rates are expensive to fetch (external HTTP).
 * Cache them so repeated calls don't hit the provider.
 *
 * TypeScript analogy:
 *   let cache = new Map<string, BigDecimal>();
 *   function getRate(currency) {
 *     if (cache.has(currency)) return cache.get(currency);
 *     const rate = provider.fetchRate(currency);
 *     cache.set(currency, rate);
 *     return rate;
 *   }
 *
 * But with Spring Cache you get: thread-safety, multiple backends (in-memory,
 * Redis, Caffeine), and zero boilerplate.
 *
 * TODO 1: Add @Cacheable(value = "rates", key = "#currency") to `getRate`.
 *         The cache name is "rates". The cache key is the currency parameter.
 *         On the second call with the same currency, Spring returns the cached value
 *         and does NOT call provider.fetchRate().
 *
 * TODO 2: Add @CachePut(value = "rates", key = "#currency") to `updateRate`.
 *         @CachePut always executes the method AND updates the cache.
 *         Use it when you want to refresh a specific cache entry.
 *
 * TODO 3: Add @CacheEvict(value = "rates", allEntries = true) to `evictAll`.
 *         This removes ALL entries from the "rates" cache.
 *         Use it when you want a full refresh (e.g., daily job).
 *
 * TODO 4: Add @CacheEvict(value = "rates", key = "#currency") to `evictCurrency`.
 *         Removes only the entry for one specific currency.
 */
@Service
public class ExchangeRateService {

    private final ExchangeRateProvider provider;

    public ExchangeRateService(ExchangeRateProvider provider) {
        this.provider = provider;
    }

    // TODO 1: @Cacheable(value = "rates", key = "#currency")
    public BigDecimal getRate(String currency) {
        return provider.fetchRate(currency);
    }

    // TODO 2: @CachePut(value = "rates", key = "#currency")
    public BigDecimal updateRate(String currency, BigDecimal newRate) {
        return newRate; // value is put into cache; no provider call needed
    }

    // TODO 3: @CacheEvict(value = "rates", allEntries = true)
    public void evictAll() {
        // Spring evicts all "rates" cache entries after this method returns
    }

    // TODO 4: @CacheEvict(value = "rates", key = "#currency")
    public void evictCurrency(String currency) {
        // Spring evicts the specific entry after this method returns
    }
}
