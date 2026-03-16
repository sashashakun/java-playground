package com.example.fintech.day2.concurrencyfoundations;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Exercise 08 — Concurrency Foundations (Part 1 of 2)
 *
 * Fetches exchange rates asynchronously using CompletableFuture.
 * Caches results in a thread-safe ConcurrentHashMap.
 * Tracks request counts with AtomicLong.
 *
 * Implement the 4 TODO methods.
 *
 * The RateFetcher is injected so tests can provide a fake/fast implementation.
 */
public class ExchangeRateService {

    /** Simulates a slow external API call. */
    @FunctionalInterface
    public interface RateFetcher {
        BigDecimal fetch(String currency); // may block
    }

    private final RateFetcher fetcher;
    private final Executor executor;
    private final ConcurrentHashMap<String, BigDecimal> cache = new ConcurrentHashMap<>();
    private final AtomicLong requestCount = new AtomicLong(0);

    public ExchangeRateService(RateFetcher fetcher, Executor executor) {
        this.fetcher = fetcher;
        this.executor = executor;
    }

    /**
     * TODO 1 — Fetch a single exchange rate asynchronously.
     *
     * Steps:
     *   1. Check the cache first: cache.get(currency) — if present, return immediately
     *      as CompletableFuture.completedFuture(cached)
     *   2. Otherwise, fetch asynchronously:
     *      CompletableFuture.supplyAsync(() -> {
     *          requestCount.incrementAndGet();
     *          BigDecimal rate = fetcher.fetch(currency);
     *          cache.put(currency, rate);
     *          return rate;
     *      }, executor)
     *
     * Note: cache.putIfAbsent would be slightly safer but supplyAsync + put is fine for this exercise.
     */
    public CompletableFuture<BigDecimal> getRate(String currency) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Fetch multiple rates concurrently and return all results.
     *
     * Steps:
     *   1. For each currency, call getRate(currency) to get a CompletableFuture
     *   2. Use CompletableFuture.allOf(...) to wait for all to complete
     *   3. After allOf completes, collect currency → rate into a Map
     *   4. Return a CompletableFuture<Map<String, BigDecimal>>
     *
     * Hint:
     *   Map<String, CompletableFuture<BigDecimal>> futures = currencies.stream()
     *       .collect(Collectors.toMap(c -> c, this::getRate));
     *
     *   CompletableFuture<Void> all = CompletableFuture.allOf(
     *       futures.values().toArray(new CompletableFuture[0]));
     *
     *   return all.thenApply(__ ->
     *       futures.entrySet().stream()
     *           .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().join())));
     */
    public CompletableFuture<Map<String, BigDecimal>> getRates(Set<String> currencies) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Fetch a rate with a fallback value on error.
     *
     * If getRate(currency) completes exceptionally, return the fallback value.
     * Use .exceptionally(ex -> fallback)
     */
    public CompletableFuture<BigDecimal> getRateOrDefault(String currency, BigDecimal fallback) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 4 — Clear the cache and return how many entries were removed.
     */
    public int clearCache() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public long getRequestCount() { return requestCount.get(); }
    public boolean isCached(String currency) { return cache.containsKey(currency); }
}
