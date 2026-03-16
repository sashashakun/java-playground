package com.example.fintech.day2.concurrencyfoundations;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ExchangeRateService {

    @FunctionalInterface
    public interface RateFetcher {
        BigDecimal fetch(String currency);
    }

    private final RateFetcher fetcher;
    private final Executor executor;
    private final ConcurrentHashMap<String, BigDecimal> cache = new ConcurrentHashMap<>();
    private final AtomicLong requestCount = new AtomicLong(0);

    public ExchangeRateService(RateFetcher fetcher, Executor executor) {
        this.fetcher = fetcher;
        this.executor = executor;
    }

    public CompletableFuture<BigDecimal> getRate(String currency) {
        BigDecimal cached = cache.get(currency);
        if (cached != null) {
            return CompletableFuture.completedFuture(cached);
        }
        return CompletableFuture.supplyAsync(() -> {
            requestCount.incrementAndGet();
            BigDecimal rate = fetcher.fetch(currency);
            cache.put(currency, rate);
            return rate;
        }, executor);
    }

    public CompletableFuture<Map<String, BigDecimal>> getRates(Set<String> currencies) {
        Map<String, CompletableFuture<BigDecimal>> futures = currencies.stream()
            .collect(Collectors.toMap(c -> c, this::getRate));

        CompletableFuture<Void> all = CompletableFuture.allOf(
            futures.values().toArray(new CompletableFuture[0]));

        return all.thenApply(__ ->
            futures.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().join())));
    }

    public CompletableFuture<BigDecimal> getRateOrDefault(String currency, BigDecimal fallback) {
        return getRate(currency).exceptionally(ex -> fallback);
    }

    public int clearCache() {
        int size = cache.size();
        cache.clear();
        return size;
    }

    public long getRequestCount() { return requestCount.get(); }
    public boolean isCached(String currency) { return cache.containsKey(currency); }
}
