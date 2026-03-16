package com.example.fintech.day2.genericsandwildcards;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public class PortfolioUtils {

    public static BigDecimal sumValues(List<? extends Number> values) {
        return values.stream()
            .map(n -> new BigDecimal(n.toString()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static <T> void copyAll(List<? extends T> source, List<? super T> destination) {
        destination.addAll(source);
    }

    public static <T extends Comparable<T>> T max(Collection<T> items) {
        return items.stream()
            .max(Comparable::compareTo)
            .orElseThrow(() -> new IllegalArgumentException("Collection is empty"));
    }

    public static <T> void swap(List<T> list, int i, int j) {
        T tmp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, tmp);
    }
}
