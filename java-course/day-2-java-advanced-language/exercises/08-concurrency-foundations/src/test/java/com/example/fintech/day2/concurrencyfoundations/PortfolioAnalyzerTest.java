package com.example.fintech.day2.concurrencyfoundations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;

class PortfolioAnalyzerTest {

    private PortfolioAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        // Rates: USD=1, EUR=1.10, BTC=65000
        ExchangeRateService.RateFetcher fetcher = currency -> switch (currency) {
            case "USD" -> BigDecimal.ONE;
            case "EUR" -> new BigDecimal("1.10");
            case "BTC" -> new BigDecimal("65000");
            default -> throw new RuntimeException("Unknown: " + currency);
        };
        var rateService = new ExchangeRateService(fetcher, Executors.newFixedThreadPool(4));
        analyzer = new PortfolioAnalyzer(rateService);
    }

    @Test
    void convertToUsdUsesRate() throws Exception {
        var asset = new PortfolioAnalyzer.Asset("a1", "EUR", new BigDecimal("100"));
        // 100 EUR * 1.10 = 110.00 USD
        BigDecimal usd = analyzer.convertToUsd(asset).get();
        assertThat(usd).isEqualByComparingTo("110.00");
    }

    @Test
    void totalValueUsdSumsConcurrently() throws Exception {
        List<PortfolioAnalyzer.Asset> assets = List.of(
            new PortfolioAnalyzer.Asset("a1", "USD", new BigDecimal("500")),
            new PortfolioAnalyzer.Asset("a2", "EUR", new BigDecimal("100")), // 110 USD
            new PortfolioAnalyzer.Asset("a3", "USD", new BigDecimal("200"))
        );
        // 500 + 110 + 200 = 810
        BigDecimal total = analyzer.totalValueUsd(assets).get();
        assertThat(total).isEqualByComparingTo("810.00");
    }

    @Test
    void breakdownByCurrencyGroupsCorrectly() throws Exception {
        List<PortfolioAnalyzer.Asset> assets = List.of(
            new PortfolioAnalyzer.Asset("a1", "USD", new BigDecimal("300")),
            new PortfolioAnalyzer.Asset("a2", "USD", new BigDecimal("200")),
            new PortfolioAnalyzer.Asset("a3", "EUR", new BigDecimal("100"))  // 110 USD
        );
        Map<String, BigDecimal> breakdown = analyzer.breakdownByCurrency(assets).get();
        assertThat(breakdown.get("USD")).isEqualByComparingTo("500.00");
        assertThat(breakdown.get("EUR")).isEqualByComparingTo("110.00");
    }

    @Test
    void totalValueUsdEmptyListReturnsZero() throws Exception {
        BigDecimal total = analyzer.totalValueUsd(List.of()).get();
        assertThat(total).isEqualByComparingTo("0");
    }
}
