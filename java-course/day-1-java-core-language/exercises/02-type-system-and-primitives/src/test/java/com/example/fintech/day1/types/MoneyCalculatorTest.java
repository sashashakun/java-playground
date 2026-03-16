package com.example.fintech.day1.types;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("MoneyCalculator")
class MoneyCalculatorTest {

    private MoneyCalculator calc;

    @BeforeEach
    void setUp() {
        calc = new MoneyCalculator();
    }

    @Test
    @DisplayName("add produces exact decimal result — no floating point error")
    void add_exactPrecision() {
        // The famous 0.1 + 0.2 test
        BigDecimal result = calc.add(new BigDecimal("0.1"), new BigDecimal("0.2"));
        assertThat(result).isEqualByComparingTo(new BigDecimal("0.3"));

        // Money addition
        assertThat(calc.add(new BigDecimal("10.50"), new BigDecimal("0.10")))
            .isEqualByComparingTo(new BigDecimal("10.60"));

        assertThat(calc.add(new BigDecimal("999.99"), new BigDecimal("0.01")))
            .isEqualByComparingTo(new BigDecimal("1000.00"));
    }

    @Test
    @DisplayName("convertCurrency multiplies by rate and rounds to specified scale")
    void convertCurrency_roundsToScale() {
        // USD 100 at rate 1.0847 = EUR 108.47
        assertThat(calc.convertCurrency(
            new BigDecimal("100.00"), new BigDecimal("1.0847"), 2))
            .isEqualByComparingTo(new BigDecimal("108.47"));

        // Round up: 100 × 1.085 = 108.5 → 108.50
        assertThat(calc.convertCurrency(
            new BigDecimal("100.00"), new BigDecimal("1.085"), 2))
            .isEqualByComparingTo(new BigDecimal("108.50"));

        // 8 decimal places (crypto)
        assertThat(calc.convertCurrency(
            new BigDecimal("1.00"), new BigDecimal("0.1"), 8))
            .isEqualByComparingTo(new BigDecimal("0.10000000"));
    }

    @Test
    @DisplayName("calculateFee applies percentage with HALF_EVEN rounding")
    void calculateFee_percentageWithBankersRounding() {
        // 1.5% of 100.00 = 1.50
        assertThat(calc.calculateFee(new BigDecimal("100.00"), new BigDecimal("1.5")))
            .isEqualByComparingTo(new BigDecimal("1.50"));

        // 10% of 33.33 = 3.333 → rounds to 3.33 (HALF_EVEN)
        assertThat(calc.calculateFee(new BigDecimal("33.33"), new BigDecimal("10.0")))
            .isEqualByComparingTo(new BigDecimal("3.33"));

        // 15% of 66.67 = 10.0005 → rounds to 10.00 (HALF_EVEN: 5 rounds to even = 0)
        assertThat(calc.calculateFee(new BigDecimal("66.67"), new BigDecimal("15.0")))
            .isEqualByComparingTo(new BigDecimal("10.00"));

        // Zero fee for zero percent
        assertThat(calc.calculateFee(new BigDecimal("100.00"), new BigDecimal("0")))
            .isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("splitAmount divides total into N parts, last absorbs remainder")
    void splitAmount_partsAlwaysSumToTotal() {
        // 10.00 / 3 = 3.33, 3.33, 3.34
        List<BigDecimal> parts = calc.splitAmount(new BigDecimal("10.00"), 3);
        assertThat(parts).hasSize(3);
        assertThat(parts.get(0)).isEqualByComparingTo(new BigDecimal("3.33"));
        assertThat(parts.get(1)).isEqualByComparingTo(new BigDecimal("3.33"));
        assertThat(parts.get(2)).isEqualByComparingTo(new BigDecimal("3.34"));

        // Sum must equal total exactly
        BigDecimal sum = parts.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        assertThat(sum).isEqualByComparingTo(new BigDecimal("10.00"));

        // Even split
        List<BigDecimal> evenParts = calc.splitAmount(new BigDecimal("1.00"), 2);
        assertThat(evenParts).hasSize(2);
        assertThat(evenParts.get(0)).isEqualByComparingTo(new BigDecimal("0.50"));
        assertThat(evenParts.get(1)).isEqualByComparingTo(new BigDecimal("0.50"));

        // 1 part
        List<BigDecimal> onePart = calc.splitAmount(new BigDecimal("99.99"), 1);
        assertThat(onePart).hasSize(1);
        assertThat(onePart.get(0)).isEqualByComparingTo(new BigDecimal("99.99"));
    }

    @Test
    @DisplayName("splitAmount throws IllegalArgumentException for zero or negative parts")
    void splitAmount_throwsForInvalidParts() {
        assertThatThrownBy(() -> calc.splitAmount(new BigDecimal("10.00"), 0))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> calc.splitAmount(new BigDecimal("10.00"), -1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("isWithinLimit compares long values correctly")
    void isWithinLimit_comparesCorrectly() {
        assertThat(calc.isWithinLimit(1000L, 5000L)).isTrue();   // under limit
        assertThat(calc.isWithinLimit(5000L, 5000L)).isTrue();   // exactly at limit
        assertThat(calc.isWithinLimit(5001L, 5000L)).isFalse();  // 1 cent over
        assertThat(calc.isWithinLimit(0L, 0L)).isTrue();         // zero vs zero
    }
}
