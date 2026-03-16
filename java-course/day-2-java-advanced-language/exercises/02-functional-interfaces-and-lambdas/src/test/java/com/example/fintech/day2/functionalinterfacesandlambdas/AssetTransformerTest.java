package com.example.fintech.day2.functionalinterfacesandlambdas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.*;

class AssetTransformerTest {

    private AssetTransformer t;

    @BeforeEach
    void setUp() { t = new AssetTransformer(); }

    private AssetTransformer.Asset asset(String id, String currency, String value, String type) {
        return new AssetTransformer.Asset(id, currency, new BigDecimal(value), type);
    }

    @Test
    void multiplierDoubles() {
        Function<BigDecimal, BigDecimal> times2 = t.multiplier(new BigDecimal("2"));
        assertThat(times2.apply(new BigDecimal("50"))).isEqualByComparingTo("100");
    }

    @Test
    void byCurrencyFiltersCorrectly() {
        Predicate<AssetTransformer.Asset> usdOnly = t.byCurrency("USD");
        assertThat(usdOnly.test(asset("a1", "USD", "100", "STOCK"))).isTrue();
        assertThat(usdOnly.test(asset("a2", "EUR", "200", "BOND"))).isFalse();
    }

    @Test
    void composeAppliesSequentially() {
        // multiply by 2 then by 3 = multiply by 6
        Function<BigDecimal, BigDecimal> times6 = t.compose(
            t.multiplier(new BigDecimal("2")),
            t.multiplier(new BigDecimal("3"))
        );
        assertThat(times6.apply(new BigDecimal("10"))).isEqualByComparingTo("60");
    }

    @Test
    void filterAndMapExtractsValues() {
        List<AssetTransformer.Asset> assets = List.of(
            asset("a1", "USD", "100", "STOCK"),
            asset("a2", "EUR", "200", "BOND"),
            asset("a3", "USD", "300", "STOCK")
        );
        List<BigDecimal> values = t.filterAndMap(assets, t.byCurrency("USD"), AssetTransformer.Asset::value);
        assertThat(values).hasSize(2).allSatisfy(v -> assertThat(v).isPositive());
    }

    @Test
    void computeFeesAppliesBiFunction() {
        List<AssetTransformer.Asset> assets = List.of(
            asset("a1", "USD", "1000", "STOCK"),
            asset("a2", "USD", "2000", "STOCK")
        );
        List<BigDecimal> fees = t.computeFees(
            assets,
            new BigDecimal("0.01"),
            (a, rate) -> a.value().multiply(rate)
        );
        assertThat(fees).hasSize(2);
        assertThat(fees.get(0)).isEqualByComparingTo("10");
        assertThat(fees.get(1)).isEqualByComparingTo("20");
    }

    @Test
    void consumeAboveCallsConsumerForHighValueOnly() {
        List<String> consumed = new ArrayList<>();
        List<AssetTransformer.Asset> assets = List.of(
            asset("a1", "USD", "500", "STOCK"),
            asset("a2", "USD", "1500", "STOCK")
        );
        int count = t.consumeAbove(assets, new BigDecimal("1000"), a -> consumed.add(a.id()));
        assertThat(count).isEqualTo(1);
        assertThat(consumed).containsExactly("a2");
    }

    @Test
    void defaultIfEmptyReturnsDefaultWhenEmpty() {
        AssetTransformer.Asset fallback = asset("DEF", "USD", "0", "CASH");
        List<AssetTransformer.Asset> result = t.defaultIfEmpty(List.of(), () -> fallback);
        assertThat(result).containsExactly(fallback);
    }

    @Test
    void defaultIfEmptyReturnsSameListWhenNonEmpty() {
        AssetTransformer.Asset a = asset("a1", "USD", "100", "STOCK");
        List<AssetTransformer.Asset> result = t.defaultIfEmpty(List.of(a), () -> {
            throw new AssertionError("supplier must not be called");
        });
        assertThat(result).containsExactly(a);
    }
}
