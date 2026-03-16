package com.example.fintech.day2.functionalinterfacesandlambdas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Exercise 02 — Functional Interfaces & Lambdas
 *
 * Work with Function, Predicate, Consumer, Supplier, BiFunction,
 * method references, and function composition.
 *
 * Asset record — provided, no changes needed.
 */
public class AssetTransformer {

    public record Asset(String id, String currency, BigDecimal value, String type) {}

    // ─── Supplied Functions (for method reference exercises) ────────────────

    /** Converts a BigDecimal to a display string: "USD 12.34" */
    public static String formatValue(String currency, BigDecimal value) {
        return "%s %s".formatted(currency, value.setScale(2, RoundingMode.HALF_EVEN).toPlainString());
    }

    /** Returns true if value > 1000 */
    public static boolean isHighValue(Asset asset) {
        return asset.value().compareTo(new BigDecimal("1000")) > 0;
    }

    // ─── TODOs ──────────────────────────────────────────────────────────────

    /**
     * TODO 1 — Return a Function that multiplies a BigDecimal by a given rate.
     *
     * Example:
     *   Function<BigDecimal, BigDecimal> toUsd = multiplier(new BigDecimal("1.08"));
     *   toUsd.apply(new BigDecimal("100.00"))  → 108.00
     *
     * Use .multiply(rate).setScale(2, RoundingMode.HALF_EVEN)
     */
    public Function<BigDecimal, BigDecimal> multiplier(BigDecimal rate) {
        // TODO: return a lambda
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Return a Predicate that checks if an asset's currency matches.
     *
     * Example:
     *   Predicate<Asset> isUsd = byCurrency("USD");
     *   isUsd.test(new Asset("a1", "USD", ...))  → true
     *   isUsd.test(new Asset("a2", "EUR", ...))  → false
     */
    public Predicate<Asset> byCurrency(String currency) {
        // TODO: return a lambda
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Compose two Functions using andThen.
     *
     * Returns a new Function that applies first, then second.
     *
     * Example:
     *   Function<String, Integer> length   = String::length;
     *   Function<Integer, String> toString = Object::toString;
     *   compose(length, toString).apply("hello")  → "5"
     *
     * Hint: first.andThen(second)
     */
    public <A, B, C> Function<A, C> compose(Function<A, B> first, Function<B, C> second) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 4 — Filter and transform a list of assets using Predicate + Function.
     *
     * Steps:
     *   1. Filter assets using the predicate
     *   2. Transform each matching asset using the function
     *   3. Return as a new List
     *
     * Example:
     *   filterAndMap(assets, byCurrency("USD"), Asset::value)
     *   → List of BigDecimal values for all USD assets
     *
     * Use streams: assets.stream().filter(pred).map(fn).toList()
     */
    public <T> List<T> filterAndMap(List<Asset> assets, Predicate<Asset> predicate,
                                    Function<Asset, T> mapper) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 5 — Apply a BiFunction to compute a fee for each asset.
     *
     * BiFunction<Asset, BigDecimal, BigDecimal> feeCalc takes an asset
     * and a fee rate, and returns the fee amount.
     *
     * Return a List of (asset.value * rate) for each asset.
     *
     * Example:
     *   computeFees(assets, new BigDecimal("0.015"),
     *       (asset, rate) -> asset.value().multiply(rate).setScale(2, HALF_EVEN))
     */
    public List<BigDecimal> computeFees(List<Asset> assets, BigDecimal rate,
                                         BiFunction<Asset, BigDecimal, BigDecimal> feeCalc) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 6 — Use a Consumer to log all assets above a value threshold.
     *
     * Apply the consumer to every asset whose value > threshold.
     * Return the count of assets that were consumed.
     *
     * Example:
     *   List<String> log = new ArrayList<>();
     *   int count = consumeAbove(assets, new BigDecimal("500"), a -> log.add(a.id()));
     *   // log now contains ids of all assets with value > 500
     */
    public int consumeAbove(List<Asset> assets, BigDecimal threshold, Consumer<Asset> action) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 7 — Use a Supplier to provide a default asset when a list is empty.
     *
     * If assets is empty, call the supplier to get a default asset and return
     * a single-element list containing it.
     * Otherwise, return the original list.
     *
     * Example:
     *   defaultIfEmpty(List.of(), () -> new Asset("DEFAULT", "USD", BigDecimal.ZERO, "CASH"))
     *   → [Asset("DEFAULT", "USD", 0, "CASH")]
     *
     *   defaultIfEmpty(List.of(asset1), () -> ...)
     *   → [asset1]  (supplier NOT called)
     */
    public List<Asset> defaultIfEmpty(List<Asset> assets, Supplier<Asset> defaultSupplier) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
