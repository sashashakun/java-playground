package com.example.fintech.day2.concurrencyfoundations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Exercise 08 — Concurrency Foundations (Part 2 of 2)
 *
 * Converts a portfolio of assets to a single "base currency" (USD)
 * by fetching exchange rates asynchronously via ExchangeRateService.
 *
 * Implement the 3 TODO methods.
 */
public class PortfolioAnalyzer {

    /** A single holding in the portfolio. */
    public record Asset(String id, String currency, BigDecimal amount) {}

    private final ExchangeRateService rateService;

    public PortfolioAnalyzer(ExchangeRateService rateService) {
        this.rateService = rateService;
    }

    /**
     * TODO 1 — Convert an asset's amount to USD.
     *
     * Steps:
     *   1. Fetch the rate for asset.currency() using rateService.getRate(...)
     *   2. thenApply: multiply asset.amount() by the rate, scale to 2dp HALF_UP
     *   3. Return the CompletableFuture<BigDecimal>
     *
     * Note: if asset.currency() is "USD", the rate should be 1.0 (handled by the fetcher).
     */
    public CompletableFuture<BigDecimal> convertToUsd(Asset asset) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Compute the total USD value of a list of assets concurrently.
     *
     * Steps:
     *   1. For each asset, call convertToUsd(asset) to get a CompletableFuture<BigDecimal>
     *   2. Wait for all futures with CompletableFuture.allOf(...)
     *   3. After allOf, sum the results using .join() on each future
     *   4. Return a CompletableFuture<BigDecimal> of the total
     *
     * Hint: collect the futures to a List<CompletableFuture<BigDecimal>> first.
     */
    public CompletableFuture<BigDecimal> totalValueUsd(List<Asset> assets) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Build a currency → total-USD-value breakdown map.
     *
     * Steps:
     *   1. Group assets by currency
     *   2. For each currency group, sum the USD values concurrently (reuse totalValueUsd)
     *   3. Combine into a Map<String, BigDecimal> (currency → total)
     *   4. Return CompletableFuture<Map<String, BigDecimal>>
     *
     * Hint:
     *   Map<String, List<Asset>> byCurrency = assets.stream()
     *       .collect(Collectors.groupingBy(Asset::currency));
     *
     *   Map<String, CompletableFuture<BigDecimal>> futures = byCurrency.entrySet().stream()
     *       .collect(Collectors.toMap(Map.Entry::getKey, e -> totalValueUsd(e.getValue())));
     *
     *   CompletableFuture<Void> all = CompletableFuture.allOf(
     *       futures.values().toArray(new CompletableFuture[0]));
     *
     *   return all.thenApply(__ ->
     *       futures.entrySet().stream()
     *           .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().join())));
     */
    public CompletableFuture<Map<String, BigDecimal>> breakdownByCurrency(List<Asset> assets) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
