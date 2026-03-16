package com.example.fintech.day2.annotationsandpackages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AnnotationScannerTest {

    private AnnotationScanner scanner;

    /** Sample service class for scanning. */
    @SuppressWarnings("unused")
    static class PaymentService {
        @RateLimit(requestsPerMinute = 10, scope = "per-user")
        @Idempotent(keyHeader = "X-Idempotency-Key", ttlSeconds = 3600)
        public void createPayment() {}

        @RateLimit(requestsPerMinute = 100)
        public void getBalance() {}

        public void ping() {}  // no annotations
    }

    @BeforeEach
    void setUp() { scanner = new AnnotationScanner(); }

    @Test
    void findRateLimitedMethodsFindsTwo() {
        List<AnnotationScanner.RateLimitedMethod> found =
            scanner.findRateLimitedMethods(PaymentService.class);
        assertThat(found).hasSize(2)
            .extracting(AnnotationScanner.RateLimitedMethod::methodName)
            .containsExactlyInAnyOrder("createPayment", "getBalance");
    }

    @Test
    void findRateLimitedMethodsReadsValues() {
        var found = scanner.findRateLimitedMethods(PaymentService.class);
        var createPayment = found.stream()
            .filter(m -> m.methodName().equals("createPayment"))
            .findFirst().orElseThrow();
        assertThat(createPayment.requestsPerMinute()).isEqualTo(10);
        assertThat(createPayment.scope()).isEqualTo("per-user");
    }

    @Test
    void getRateLimitReturnsPresentForAnnotated() {
        var rl = scanner.getRateLimit(PaymentService.class, "getBalance");
        assertThat(rl).isPresent();
        assertThat(rl.get().requestsPerMinute()).isEqualTo(100);
        assertThat(rl.get().scope()).isEqualTo("global"); // default
    }

    @Test
    void getRateLimitReturnsEmptyForUnannotated() {
        assertThat(scanner.getRateLimit(PaymentService.class, "ping")).isEmpty();
    }

    @Test
    void getRateLimitReturnsEmptyForMissingMethod() {
        assertThat(scanner.getRateLimit(PaymentService.class, "noSuchMethod")).isEmpty();
    }

    @Test
    void isIdempotentTrueForAnnotated() {
        assertThat(scanner.isIdempotent(PaymentService.class, "createPayment")).isTrue();
    }

    @Test
    void isIdempotentFalseForUnannotated() {
        assertThat(scanner.isIdempotent(PaymentService.class, "getBalance")).isFalse();
        assertThat(scanner.isIdempotent(PaymentService.class, "ping")).isFalse();
    }

    @Test
    void totalRateLimitBudgetSumsAll() {
        // 10 + 100 = 110
        assertThat(scanner.totalRateLimitBudget(PaymentService.class)).isEqualTo(110);
    }

    @Test
    void totalRateLimitBudgetZeroWhenNone() {
        class NoAnnotations { public void foo() {} }
        assertThat(scanner.totalRateLimitBudget(NoAnnotations.class)).isEqualTo(0);
    }
}
