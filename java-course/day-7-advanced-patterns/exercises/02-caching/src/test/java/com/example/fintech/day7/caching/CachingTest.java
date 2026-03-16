package com.example.fintech.day7.caching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CachingTest {

    /** In-test provider counts how many times fetchRate is actually called. */
    @TestConfiguration
    static class TestConfig {
        static final AtomicInteger callCount = new AtomicInteger(0);

        @Bean
        @Primary
        ExchangeRateProvider countingProvider() {
            callCount.set(0);
            return currency -> {
                callCount.incrementAndGet();
                return switch (currency) {
                    case "EUR" -> new BigDecimal("1.08");
                    case "GBP" -> new BigDecimal("1.27");
                    default   -> BigDecimal.ONE;
                };
            };
        }
    }

    @Autowired ExchangeRateService service;
    @Autowired CacheManager cacheManager;

    @BeforeEach
    void resetCallCount() {
        TestConfig.callCount.set(0);
        // also clear the cache to start fresh
        var cache = cacheManager.getCache("rates");
        if (cache != null) cache.clear();
    }

    @Test
    void getRate_cachesPreviousCalls() {
        BigDecimal first  = service.getRate("EUR");
        BigDecimal second = service.getRate("EUR"); // should hit cache

        assertThat(first).isEqualByComparingTo("1.08");
        assertThat(second).isEqualByComparingTo("1.08");
        assertThat(TestConfig.callCount.get()).isEqualTo(1); // provider called only once
    }

    @Test
    void getRate_differentCurrenciesAreCachedSeparately() {
        service.getRate("EUR");
        service.getRate("GBP");
        service.getRate("EUR"); // cache hit

        assertThat(TestConfig.callCount.get()).isEqualTo(2); // EUR + GBP, not EUR again
    }

    @Test
    void updateRate_putsCacheEntryWithoutCallingProvider() {
        service.updateRate("USD", new BigDecimal("1.00"));
        BigDecimal cached = service.getRate("USD"); // should return cached value

        assertThat(cached).isEqualByComparingTo("1.00");
        assertThat(TestConfig.callCount.get()).isEqualTo(0); // provider never called
    }

    @Test
    void evictAll_clearsCacheForAllCurrencies() {
        service.getRate("EUR");
        service.getRate("GBP");
        assertThat(TestConfig.callCount.get()).isEqualTo(2);

        service.evictAll();

        service.getRate("EUR"); // cache miss — must call provider again
        assertThat(TestConfig.callCount.get()).isEqualTo(3);
    }

    @Test
    void evictCurrency_clearsOnlyThatEntry() {
        service.getRate("EUR");
        service.getRate("GBP");
        assertThat(TestConfig.callCount.get()).isEqualTo(2);

        service.evictCurrency("EUR");

        service.getRate("EUR"); // cache miss
        service.getRate("GBP"); // cache hit — GBP was NOT evicted
        assertThat(TestConfig.callCount.get()).isEqualTo(3);
    }
}
