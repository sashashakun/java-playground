package com.example.fintech.day6.parameterized;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Exercise 01 — JUnit 5 Parameterized Tests
 *
 * Replace each TODO stub with a real parameterized (or structured) test.
 * Run `./gradlew test` to see failures; implement until all pass.
 *
 * TypeScript analogy:
 *   @ParameterizedTest + @CsvSource  ≈  test.each([…])("desc", (a,b) => …)
 *   @Nested class                    ≈  describe("Currency…", () => { … })
 *   @TestFactory                     ≈  dynamically generating test cases at runtime
 */
class PaymentValidatorTest {

    private final PaymentValidator validator = new PaymentValidator();

    // ─────────────────────────────────────────────────────────────────────────
    // TODO 1 — @CsvSource parameterized test
    //
    // Write a single @ParameterizedTest that covers all these cases without
    // copy-pasting four separate @Test methods:
    //
    //  amount        | currency | description | expectedValid
    //  100.00        | USD      | "purchase"  | true
    //  0.01          | EUR      | "min"       | true   (boundary: minimum amount)
    //  50000.00      | GBP      | "max"       | true   (boundary: maximum amount)
    //  0.009         | USD      | "too small" | false  (below minimum)
    //  50000.01      | USD      | "too big"   | false  (above maximum)
    //
    // Hint: use @CsvSource({"100.00,USD,purchase,true", ...})
    //       The test method should take (BigDecimal amount, String currency,
    //       String description, boolean expectedValid).
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void TODO1_csvSourceParameterizedTest() {
        throw new UnsupportedOperationException("TODO 1: replace with @ParameterizedTest @CsvSource");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TODO 2 — @MethodSource parameterized test
    //
    // @CsvSource can't express null. Use @MethodSource to cover null cases:
    //   - null amount     → not valid, errorCode = AMOUNT_NULL
    //   - null currency   → not valid, errorCode = CURRENCY_NULL
    //   - null description→ not valid, errorCode = DESCRIPTION_NULL
    //
    // Create a static Stream<Arguments> factory method called `nullInputCases`
    // that returns the three cases above as Arguments.of(amount, currency, desc, errorCode).
    // Annotate the test with @ParameterizedTest @MethodSource("nullInputCases").
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void TODO2_methodSourceWithNullCases() {
        throw new UnsupportedOperationException("TODO 2: replace with @ParameterizedTest @MethodSource");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TODO 3 — @EnumSource parameterized test
    //
    // `isSupportedCurrency` should return true for USD, EUR, GBP, CHF and
    // false for anything else.
    //
    // Use @ParameterizedTest + @ValueSource(strings = {"USD","EUR","GBP","CHF"})
    // to verify that each returns true.
    //
    // Then write a SECOND parameterized test using @ValueSource for unsupported
    // values: {"JPY", "BTC", "XYZ", ""} → all return false.
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void TODO3_valueSourceForCurrencies() {
        throw new UnsupportedOperationException("TODO 3: replace with two @ParameterizedTest @ValueSource tests");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TODO 4 — @Nested class
    //
    // Organise the error-code tests into a @Nested inner class called
    // `ErrorCodeTests`. Inside it, write individual @Test methods (or
    // parameterized tests) that verify:
    //   1. Amount = 0 → errorCode AMOUNT_TOO_LOW
    //   2. Amount = 99999999 → errorCode AMOUNT_TOO_HIGH
    //   3. Currency = "JPY" → errorCode CURRENCY_NOT_SUPPORTED
    //   4. Description of 201 chars → errorCode DESCRIPTION_TOO_LONG
    //
    // Benefit: IntelliJ and Gradle display these as a collapsible "ErrorCodeTests"
    // group — like a describe() block in Jest.
    // ─────────────────────────────────────────────────────────────────────────

    // TODO 4: Create a @Nested class ErrorCodeTests here with 4 @Test methods

    // ─────────────────────────────────────────────────────────────────────────
    // TODO 5 — @TestFactory (dynamic tests)
    //
    // @TestFactory lets you generate test cases at runtime from data.
    // Write a @TestFactory method called `dynamicAmountBoundaryTests` that
    // returns a Stream<DynamicTest>. Generate one DynamicTest for each of
    // these (description, amount, expected) triples:
    //   "just below min"  → 0.009   → false
    //   "exactly min"     → 0.01    → true
    //   "exactly max"     → 50000   → true
    //   "just above max"  → 50000.01→ false
    //
    // Use DynamicTest.dynamicTest(displayName, executable).
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    void TODO5_testFactory() {
        throw new UnsupportedOperationException("TODO 5: replace with @TestFactory");
    }
}
