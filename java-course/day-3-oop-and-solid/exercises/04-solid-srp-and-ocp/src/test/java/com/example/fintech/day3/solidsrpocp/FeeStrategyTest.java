package com.example.fintech.day3.solidsrpocp;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class FeeStrategyTest {

    // ─── Individual strategies ────────────────────────────────────────────────

    @Test
    void purchaseFeeIs1point5Percent() {
        BigDecimal fee = new FeeStrategies.PurchaseFeeStrategy()
            .calculate(new BigDecimal("1000"));
        assertThat(fee).isEqualByComparingTo("15.00");
    }

    @Test
    void transferFeeIsHalfPercent() {
        BigDecimal fee = new FeeStrategies.TransferFeeStrategy()
            .calculate(new BigDecimal("1000"));
        assertThat(fee).isEqualByComparingTo("5.00");
    }

    @Test
    void cryptoFeeIsHalfPercentMinimum50Cents() {
        // 0.5% of 10 = 0.05 → less than min 0.50 → returns 0.50
        BigDecimal feeSmall = new FeeStrategies.CryptoFeeStrategy()
            .calculate(new BigDecimal("10"));
        assertThat(feeSmall).isEqualByComparingTo("0.50");

        // 0.5% of 1000 = 5.00 → above min
        BigDecimal feeLarge = new FeeStrategies.CryptoFeeStrategy()
            .calculate(new BigDecimal("1000"));
        assertThat(feeLarge).isEqualByComparingTo("5.00");
    }

    @Test
    void zeroFeeIsAlwaysZero() {
        BigDecimal fee = new FeeStrategies.ZeroFeeStrategy()
            .calculate(new BigDecimal("99999"));
        assertThat(fee).isEqualByComparingTo("0");
    }

    // ─── Registry ─────────────────────────────────────────────────────────────

    @Test
    void registryLooksUpRegisteredStrategy() {
        var registry = new FeeStrategyRegistry();
        registry.register("PURCHASE", new FeeStrategies.PurchaseFeeStrategy());
        assertThat(registry.getStrategy("PURCHASE")).isPresent();
    }

    @Test
    void registryReturnsEmptyForUnknownType() {
        var registry = new FeeStrategyRegistry();
        assertThat(registry.getStrategy("UNKNOWN")).isEmpty();
    }

    @Test
    void registryCalculatesFeeForKnownType() {
        var registry = FeeStrategyRegistry.defaultRegistry();
        BigDecimal fee = registry.calculateFee("PURCHASE", new BigDecimal("200"));
        assertThat(fee).isEqualByComparingTo("3.00"); // 1.5%
    }

    @Test
    void registryThrowsForUnknownTypeInCalculate() {
        var registry = FeeStrategyRegistry.defaultRegistry();
        assertThatThrownBy(() -> registry.calculateFee("MYSTERY", new BigDecimal("100")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("MYSTERY");
    }

    @Test
    void defaultRegistryHasAllExpectedTypes() {
        var registry = FeeStrategyRegistry.defaultRegistry();
        assertThat(registry.getStrategy("PURCHASE")).isPresent();
        assertThat(registry.getStrategy("TRANSFER")).isPresent();
        assertThat(registry.getStrategy("CRYPTO")).isPresent();
        assertThat(registry.getStrategy("FEE")).isPresent();
        assertThat(registry.getStrategy("REFUND")).isPresent();
    }

    @Test
    void defaultRegistryCryptoFee() {
        var registry = FeeStrategyRegistry.defaultRegistry();
        // 0.5% of 2000 = 10.00
        assertThat(registry.calculateFee("CRYPTO", new BigDecimal("2000")))
            .isEqualByComparingTo("10.00");
    }
}
