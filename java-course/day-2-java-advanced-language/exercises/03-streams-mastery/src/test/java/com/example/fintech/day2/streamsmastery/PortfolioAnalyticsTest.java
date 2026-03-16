package com.example.fintech.day2.streamsmastery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class PortfolioAnalyticsTest {

    private PortfolioAnalytics analytics;

    private static final List<PortfolioAnalytics.Asset> ASSETS = List.of(
        new PortfolioAnalytics.Asset("a1", "USD", "STOCK",  1000.0),
        new PortfolioAnalytics.Asset("a2", "EUR", "BOND",    500.0),
        new PortfolioAnalytics.Asset("a3", "USD", "STOCK",  2000.0),
        new PortfolioAnalytics.Asset("a4", "BTC", "CRYPTO", 45000.0),
        new PortfolioAnalytics.Asset("a5", "EUR", "BOND",    300.0),
        new PortfolioAnalytics.Asset("a6", "USD", "CRYPTO",  8000.0)
    );

    @BeforeEach
    void setUp() { analytics = new PortfolioAnalytics(); }

    @Test
    void totalValueSumsAllAssets() {
        assertThat(analytics.totalValue(ASSETS)).isEqualTo(56800.0, within(0.001));
    }

    @Test
    void countByCurrencyGroupsCorrectly() {
        Map<String, Long> counts = analytics.countByCurrency(ASSETS);
        assertThat(counts).containsEntry("USD", 3L)
                          .containsEntry("EUR", 2L)
                          .containsEntry("BTC", 1L);
    }

    @Test
    void totalValueByTypeAggregates() {
        Map<String, Double> byType = analytics.totalValueByType(ASSETS);
        assertThat(byType.get("STOCK")).isEqualTo(3000.0, within(0.001));
        assertThat(byType.get("BOND")).isEqualTo(800.0, within(0.001));
        assertThat(byType.get("CRYPTO")).isEqualTo(53000.0, within(0.001));
    }

    @Test
    void topNReturnsLargestDescending() {
        List<PortfolioAnalytics.Asset> top2 = analytics.topN(ASSETS, 2);
        assertThat(top2).hasSize(2);
        assertThat(top2.get(0).id()).isEqualTo("a4"); // 45000
        assertThat(top2.get(1).id()).isEqualTo("a6"); // 8000
    }

    @Test
    void summarizeByTypeHasCorrectStats() {
        var stats = analytics.summarizeByType(ASSETS);
        assertThat(stats.get("BOND").getCount()).isEqualTo(2);
        assertThat(stats.get("BOND").getSum()).isEqualTo(800.0, within(0.001));
    }

    @Test
    void allCurrenciesFlattenAndDeduplicates() {
        List<List<PortfolioAnalytics.Asset>> portfolios = List.of(
            List.of(new PortfolioAnalytics.Asset("x1", "USD", "STOCK", 100),
                    new PortfolioAnalytics.Asset("x2", "EUR", "BOND", 200)),
            List.of(new PortfolioAnalytics.Asset("x3", "USD", "STOCK", 300),
                    new PortfolioAnalytics.Asset("x4", "BTC", "CRYPTO", 400))
        );
        List<String> currencies = analytics.allCurrencies(portfolios);
        assertThat(currencies).containsExactly("BTC", "EUR", "USD"); // sorted
    }

    @Test
    void partitionSplitsByThreshold() {
        Map<Boolean, List<PortfolioAnalytics.Asset>> parts = analytics.partition(ASSETS, 1000.0);
        assertThat(parts.get(true)).extracting(PortfolioAnalytics.Asset::id)
            .containsExactlyInAnyOrder("a1", "a3", "a4", "a6"); // >= 1000
        assertThat(parts.get(false)).extracting(PortfolioAnalytics.Asset::id)
            .containsExactlyInAnyOrder("a2", "a5"); // < 1000
    }

    @Test
    void highestValueOfTypeFindsCorrect() {
        assertThat(analytics.highestValueOfType(ASSETS, "BOND"))
            .isPresent()
            .map(PortfolioAnalytics.Asset::id)
            .contains("a2"); // EUR bond at 500
    }

    @Test
    void highestValueOfTypeMissingReturnsEmpty() {
        assertThat(analytics.highestValueOfType(ASSETS, "REAL_ESTATE")).isEmpty();
    }
}
