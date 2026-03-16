package com.example.fintech.day7.retry;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * We test retry behaviour by subclassing PaymentGatewayClient so we can
 * control which calls fail. The Spring context loads the subclass as a primary bean.
 */
@SpringBootTest
class SpringRetryTest {

    /** A client that fails N times before succeeding. */
    static class FlakeyClient extends PaymentGatewayClient {
        final int failuresBeforeSuccess;
        final AtomicInteger attempts = new AtomicInteger(0);

        FlakeyClient(int failuresBeforeSuccess) {
            this.failuresBeforeSuccess = failuresBeforeSuccess;
        }

        @Override
        public ChargeResult charge(BigDecimal amount, String currency) {
            int attempt = attempts.incrementAndGet();
            if (attempt <= failuresBeforeSuccess) {
                throw new GatewayException("Gateway timeout (attempt " + attempt + ")");
            }
            return ChargeResult.success("txn-" + UUID.randomUUID().toString().substring(0, 8),
                amount, currency);
        }
    }

    @TestConfiguration
    static class SucceedOnThirdConfig {
        @Bean
        @Primary
        PaymentGatewayClient gatewayClientSucceedsOnThird() {
            return new FlakeyClient(2); // fail 2 times, succeed on attempt 3
        }
    }

    @Autowired
    PaymentGatewayClient client;

    @Test
    void charge_retriesOnGatewayException_succeedsOnThirdAttempt() {
        ChargeResult result = client.charge(new BigDecimal("100.00"), "USD");

        assertThat(result.status()).isEqualTo("SUCCESS");
        // The FlakeyClient tracks attempts; cast to check
        FlakeyClient flakey = (FlakeyClient) client;
        assertThat(flakey.attempts.get()).isEqualTo(3); // 2 failures + 1 success
    }

    @SpringBootTest
    static class RecoverTest {

        @TestConfiguration
        static class AlwaysFailConfig {
            @Bean
            @Primary
            PaymentGatewayClient alwaysFailing() {
                return new FlakeyClient(999); // always fails (more than maxAttempts)
            }
        }

        @Autowired
        PaymentGatewayClient client;

        @Test
        void charge_exhaustsRetries_callsRecoverFallback() {
            ChargeResult result = client.charge(new BigDecimal("250.00"), "EUR");

            // @Recover should have been called, returning a QUEUED status
            assertThat(result.status()).isEqualTo("QUEUED");
            assertThat(result.transactionId()).isEqualTo("FALLBACK-QUEUED");

            // Verify exactly 3 attempts were made (maxAttempts=3)
            FlakeyClient flakey = (FlakeyClient) client;
            assertThat(flakey.attempts.get()).isEqualTo(3);
        }
    }
}
