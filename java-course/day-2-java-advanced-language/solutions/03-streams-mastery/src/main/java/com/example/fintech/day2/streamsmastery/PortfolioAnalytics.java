package com.example.fintech.day2.streamsmastery;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PortfolioAnalytics {

    public record Asset(String id, String currency, String type, double valueUsd) {}

    public double totalValue(List<Asset> assets) {
        return assets.stream().mapToDouble(Asset::valueUsd).sum();
    }

    public Map<String, Long> countByCurrency(List<Asset> assets) {
        return assets.stream().collect(Collectors.groupingBy(Asset::currency, Collectors.counting()));
    }

    public Map<String, Double> totalValueByType(List<Asset> assets) {
        return assets.stream()
            .collect(Collectors.groupingBy(Asset::type, Collectors.summingDouble(Asset::valueUsd)));
    }

    public List<Asset> topN(List<Asset> assets, int n) {
        return assets.stream()
            .sorted(Comparator.comparingDouble(Asset::valueUsd).reversed())
            .limit(n)
            .toList();
    }

    public Map<String, DoubleSummaryStatistics> summarizeByType(List<Asset> assets) {
        return assets.stream()
            .collect(Collectors.groupingBy(Asset::currency, Collectors.summarizingDouble(Asset::valueUsd)));
    }

    public List<String> allCurrencies(List<List<Asset>> portfolios) {
        return portfolios.stream()
            .flatMap(List::stream)
            .map(Asset::currency)
            .distinct()
            .sorted()
            .toList();
    }

    public Map<Boolean, List<Asset>> partition(List<Asset> assets, double threshold) {
        return assets.stream()
            .collect(Collectors.partitioningBy(a -> a.valueUsd() >= threshold));
    }

    public Optional<Asset> highestValueOfType(List<Asset> assets, String type) {
        return assets.stream()
            .filter(a -> a.type().equals(type))
            .max(Comparator.comparingDouble(Asset::valueUsd));
    }
}
