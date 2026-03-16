package com.example.fintech.day2.genericsandwildcards;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PortfolioUtilsTest {

    @Test
    void sumValuesIntegers() {
        assertThat(PortfolioUtils.sumValues(List.of(1, 2, 3, 4))).isEqualByComparingTo("10");
    }

    @Test
    void sumValuesBigDecimals() {
        assertThat(PortfolioUtils.sumValues(
            List.of(new BigDecimal("1.5"), new BigDecimal("2.5"), new BigDecimal("3.0"))))
            .isEqualByComparingTo("7.0");
    }

    @Test
    void sumValuesEmpty() {
        assertThat(PortfolioUtils.sumValues(List.of())).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void copyAllTransfers() {
        List<Integer> src = List.of(10, 20, 30);
        List<Number> dst = new ArrayList<>();
        PortfolioUtils.copyAll(src, dst);
        assertThat(dst).containsExactly(10, 20, 30);
    }

    @Test
    void maxFindsLargest() {
        assertThat(PortfolioUtils.max(List.of(3, 1, 4, 1, 5, 9, 2))).isEqualTo(9);
    }

    @Test
    void maxSingleElement() {
        assertThat(PortfolioUtils.max(List.of("only"))).isEqualTo("only");
    }

    @Test
    void swapElements() {
        List<String> list = new ArrayList<>(List.of("a", "b", "c"));
        PortfolioUtils.swap(list, 0, 2);
        assertThat(list).containsExactly("c", "b", "a");
    }
}
