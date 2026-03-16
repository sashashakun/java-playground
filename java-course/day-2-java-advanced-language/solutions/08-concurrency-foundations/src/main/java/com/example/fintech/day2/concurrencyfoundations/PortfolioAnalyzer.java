package com.example.fintech.day2.concurrencyfoundations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PortfolioAnalyzer {

    public record Asset(String id, String currency, BigDecimal amount) {}

    private final ExchangeRateService rateService;

    public PortfolioAnalyzer(ExchangeRateService rateService) {
        this.rateService = rateService;
    }

    public CompletableFuture<BigDecimal> convertToUsd(Asset asset) {
        return rateService.getRate(asset.currency())
            .thenApply(rate -> asset.amount().multiply(rate).setScale(2, RoundingMode.HALF_UP));
    }

    public CompletableFuture<BigDecimal> totalValueUsd(List<Asset> assets) {
        if (assets.isEmpty()) {
            return CompletableFuture.completedFuture(BigDecimal.ZERO);
        }
        List<CompletableFuture<BigDecimal>> futures = assets.stream()
            .map(this::convertToUsd)
            .toList();

        CompletableFuture<Void> all = CompletableFuture.allOf(
            futures.toArray(new CompletableFuture[0]));

        return all.thenApply(__ ->
            futures.stream()
                .map(CompletableFuture::join)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    public CompletableFuture<Map<String, BigDecimal>> breakdownByCurrency(List<Asset> assets) {
        Map<String, List<Asset>> byCurrency = assets.stream()
            .collect(Collectors.groupingBy(Asset::currency));

        Map<String, CompletableFuture<BigDecimal>> futures = byCurrency.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> totalValueUsd(e.getValue())));

        CompletableFuture<Void> all = CompletableFuture.allOf(
            futures.values().toArray(new CompletableFuture[0]));

        return all.thenApply(__ ->
            futures.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().join())));
    }
}
