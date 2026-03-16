package com.example.fintech.day6.parameterized;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentValidatorTest {

    private final PaymentValidator validator = new PaymentValidator();

    // ── TODO 1 solution ──────────────────────────────────────────────────────
    @ParameterizedTest(name = "[{index}] amount={0}, currency={1} → valid={3}")
    @CsvSource({
        "100.00, USD, purchase, true",
        "0.01,   EUR, min,      true",
        "50000.00, GBP, max,    true",
        "0.009, USD, too small, false",
        "50000.01, USD, too big, false"
    })
    void csvSource_validatesAmountBoundaries(BigDecimal amount, String currency,
                                             String description, boolean expectedValid) {
        PaymentValidator.ValidationResult result = validator.validate(amount, currency, description);
        assertThat(result.valid()).isEqualTo(expectedValid);
    }

    // ── TODO 2 solution ──────────────────────────────────────────────────────
    static Stream<Arguments> nullInputCases() {
        return Stream.of(
            Arguments.of(null, "USD", "desc", "AMOUNT_NULL"),
            Arguments.of(new BigDecimal("100"), null, "desc", "CURRENCY_NULL"),
            Arguments.of(new BigDecimal("100"), "USD", null, "DESCRIPTION_NULL")
        );
    }

    @ParameterizedTest(name = "[{index}] → errorCode={3}")
    @MethodSource("nullInputCases")
    void methodSource_nullInputsReturnExpectedErrorCodes(BigDecimal amount, String currency,
                                                          String description, String expectedCode) {
        PaymentValidator.ValidationResult result = validator.validate(amount, currency, description);
        assertThat(result.valid()).isFalse();
        assertThat(result.errorCode()).isEqualTo(expectedCode);
    }

    // ── TODO 3 solution ──────────────────────────────────────────────────────
    @ParameterizedTest
    @ValueSource(strings = {"USD", "EUR", "GBP", "CHF"})
    void supportedCurrencies_returnTrue(String currency) {
        assertThat(validator.isSupportedCurrency(currency)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"JPY", "BTC", "XYZ", ""})
    void unsupportedCurrencies_returnFalse(String currency) {
        assertThat(validator.isSupportedCurrency(currency)).isFalse();
    }

    // ── TODO 4 solution ──────────────────────────────────────────────────────
    @Nested
    @DisplayName("ErrorCode tests")
    class ErrorCodeTests {

        @Test
        void zeroAmount_returnsTooLow() {
            PaymentValidator.ValidationResult r = validator.validate(BigDecimal.ZERO, "USD", "x");
            assertThat(r.errorCode()).isEqualTo(PaymentValidator.ErrorCode.AMOUNT_TOO_LOW.name());
        }

        @Test
        void hugeAmount_returnsTooHigh() {
            PaymentValidator.ValidationResult r = validator.validate(new BigDecimal("99999999"), "USD", "x");
            assertThat(r.errorCode()).isEqualTo(PaymentValidator.ErrorCode.AMOUNT_TOO_HIGH.name());
        }

        @Test
        void unsupportedCurrency_returnsCurrencyNotSupported() {
            PaymentValidator.ValidationResult r = validator.validate(new BigDecimal("100"), "JPY", "x");
            assertThat(r.errorCode()).isEqualTo(PaymentValidator.ErrorCode.CURRENCY_NOT_SUPPORTED.name());
        }

        @Test
        void tooLongDescription_returnsDescriptionTooLong() {
            String longDesc = "x".repeat(201);
            PaymentValidator.ValidationResult r = validator.validate(new BigDecimal("100"), "USD", longDesc);
            assertThat(r.errorCode()).isEqualTo(PaymentValidator.ErrorCode.DESCRIPTION_TOO_LONG.name());
        }
    }

    // ── TODO 5 solution ──────────────────────────────────────────────────────
    @TestFactory
    Stream<DynamicTest> dynamicAmountBoundaryTests() {
        record TestCase(String name, String amount, boolean expected) {}

        return Stream.of(
            new TestCase("just below min",  "0.009",    false),
            new TestCase("exactly min",     "0.01",     true),
            new TestCase("exactly max",     "50000.00", true),
            new TestCase("just above max",  "50000.01", false)
        ).map(tc -> DynamicTest.dynamicTest(tc.name(), () -> {
            PaymentValidator.ValidationResult result =
                validator.validate(new BigDecimal(tc.amount()), "USD", "boundary test");
            assertThat(result.valid()).isEqualTo(tc.expected());
        }));
    }
}
