package com.example.fintech.day1.tooling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for Exercise 01 — Tooling & Gradle.
 * Run with: ./gradlew test
 *
 * All 5 tests should pass when you implement TransactionSummary correctly.
 */
@DisplayName("TransactionSummary")
class TransactionSummaryTest {

    private TransactionSummary summary;

    @BeforeEach
    void setUp() {
        summary = new TransactionSummary();
    }

    @Test
    @DisplayName("countPositive returns count of amounts greater than zero")
    void countPositive_countsStrictlyPositive() {
        assertThat(summary.countPositive(new int[]{100, -50, 200, 0, 300})).isEqualTo(3);
        assertThat(summary.countPositive(new int[]{-1, -2, -3})).isEqualTo(0);
        assertThat(summary.countPositive(new int[]{0, 0, 0})).isEqualTo(0);
        assertThat(summary.countPositive(new int[]{})).isEqualTo(0);
        assertThat(summary.countPositive(new int[]{1})).isEqualTo(1);
    }

    @Test
    @DisplayName("sumAmounts returns total of all amounts as long")
    void sumAmounts_sumsAllAmounts() {
        assertThat(summary.sumAmounts(new long[]{100L, 200L, 300L})).isEqualTo(600L);
        assertThat(summary.sumAmounts(new long[]{-50L, 150L})).isEqualTo(100L);
        assertThat(summary.sumAmounts(new long[]{})).isEqualTo(0L);
        // Long arithmetic — this would overflow int but not long
        assertThat(summary.sumAmounts(new long[]{Integer.MAX_VALUE, Integer.MAX_VALUE}))
            .isEqualTo(4_294_967_294L);
    }

    @Test
    @DisplayName("formatAmount converts cents to display string with currency prefix")
    void formatAmount_formatsCorrectly() {
        assertThat(summary.formatAmount(1599L, "USD")).isEqualTo("USD 15.99");
        assertThat(summary.formatAmount(100L,  "EUR")).isEqualTo("EUR 1.00");
        assertThat(summary.formatAmount(0L,    "CHF")).isEqualTo("CHF 0.00");
        assertThat(summary.formatAmount(1L,    "GBP")).isEqualTo("GBP 0.01");
        assertThat(summary.formatAmount(100000L, "USD")).isEqualTo("USD 1000.00");
    }

    @Test
    @DisplayName("reverseDescription reverses a string, handles null as empty")
    void reverseDescription_handlesAllInputs() {
        assertThat(summary.reverseDescription("Coffee")).isEqualTo("eeffoC");
        assertThat(summary.reverseDescription("abc")).isEqualTo("cba");
        assertThat(summary.reverseDescription("a")).isEqualTo("a");
        assertThat(summary.reverseDescription("")).isEqualTo("");
        assertThat(summary.reverseDescription(null)).isEqualTo("");
    }

    @Test
    @DisplayName("isValidCurrency validates ISO 4217 currency codes")
    void isValidCurrency_validatesFormat() {
        // Valid codes
        assertThat(summary.isValidCurrency("USD")).isTrue();
        assertThat(summary.isValidCurrency("EUR")).isTrue();
        assertThat(summary.isValidCurrency("BTC")).isTrue();
        assertThat(summary.isValidCurrency("CHF")).isTrue();

        // Invalid codes
        assertThat(summary.isValidCurrency("usd")).isFalse();  // lowercase
        assertThat(summary.isValidCurrency("US")).isFalse();   // too short
        assertThat(summary.isValidCurrency("USDA")).isFalse(); // too long
        assertThat(summary.isValidCurrency("1USD")).isFalse(); // starts with digit
        assertThat(summary.isValidCurrency("")).isFalse();     // empty
        assertThat(summary.isValidCurrency(null)).isFalse();   // null
        assertThat(summary.isValidCurrency("U S")).isFalse();  // space
    }
}
