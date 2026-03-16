package com.example.fintech.day2.functionalinterfacesandlambdas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class AssetTransformer {

    public record Asset(String id, String currency, BigDecimal value, String type) {}

    public static String formatValue(String currency, BigDecimal value) {
        return "%s %s".formatted(currency, value.setScale(2, RoundingMode.HALF_EVEN).toPlainString());
    }

    public static boolean isHighValue(Asset asset) {
        return asset.value().compareTo(new BigDecimal("1000")) > 0;
    }

    public Function<BigDecimal, BigDecimal> multiplier(BigDecimal rate) {
        return amount -> amount.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
    }

    public Predicate<Asset> byCurrency(String currency) {
        return asset -> asset.currency().equals(currency);
    }

    public <A, B, C> Function<A, C> compose(Function<A, B> first, Function<B, C> second) {
        return first.andThen(second);
    }

    public <T> List<T> filterAndMap(List<Asset> assets, Predicate<Asset> predicate,
                                    Function<Asset, T> mapper) {
        return assets.stream().filter(predicate).map(mapper).toList();
    }

    public List<BigDecimal> computeFees(List<Asset> assets, BigDecimal rate,
                                         BiFunction<Asset, BigDecimal, BigDecimal> feeCalc) {
        return assets.stream().map(a -> feeCalc.apply(a, rate)).toList();
    }

    public int consumeAbove(List<Asset> assets, BigDecimal threshold, Consumer<Asset> action) {
        List<Asset> matching = assets.stream()
            .filter(a -> a.value().compareTo(threshold) > 0)
            .toList();
        matching.forEach(action);
        return matching.size();
    }

    public List<Asset> defaultIfEmpty(List<Asset> assets, Supplier<Asset> defaultSupplier) {
        if (assets.isEmpty()) {
            return List.of(defaultSupplier.get());
        }
        return assets;
    }
}
