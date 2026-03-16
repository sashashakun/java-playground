package com.example.fintech.day4.configprops;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PaymentConfigTest {

    @Autowired PaymentConfig config;
    @Autowired PaymentLimitService service;

    @Test
    void configLoadsMaxAmount() {
        assertThat(config.getMaxAmount()).isEqualByComparingTo("50000.00");
    }

    @Test
    void configLoadsSupportedCurrencies() {
        assertThat(config.getSupportedCurrencies())
            .containsExactlyInAnyOrder("USD", "EUR", "GBP", "CHF");
    }

    @Test
    void configLoadsDefaultFeeRate() {
        assertThat(config.getDefaultFeeRate()).isEqualByComparingTo("0.015");
    }

    @Test
    void configLoadsGatewayUrl() {
        assertThat(config.getGatewayUrl()).contains("example-gateway.com");
    }

    @Test
    void serviceIsWithinLimitForSmallAmount() {
        assertThat(service.isWithinLimit(new BigDecimal("100"))).isTrue();
    }

    @Test
    void serviceExceedsLimitForLargeAmount() {
        assertThat(service.isWithinLimit(new BigDecimal("99999"))).isFalse();
    }

    @Test
    void serviceRecognizesSupportedCurrency() {
        assertThat(service.isSupportedCurrency("USD")).isTrue();
        assertThat(service.isSupportedCurrency("EUR")).isTrue();
    }

    @Test
    void serviceRejectsUnsupportedCurrency() {
        assertThat(service.isSupportedCurrency("JPY")).isFalse();
    }

    @Test
    void serviceCalculatesFee() {
        // 1.5% of 1000 = 15.00
        assertThat(service.calculateFee(new BigDecimal("1000"))).isEqualByComparingTo("15.00");
    }
}
