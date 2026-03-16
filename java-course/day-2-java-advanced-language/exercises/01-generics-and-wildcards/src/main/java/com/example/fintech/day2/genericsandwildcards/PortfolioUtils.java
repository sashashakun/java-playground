package com.example.fintech.day2.genericsandwildcards;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * Exercise 01 — Generics & Wildcards (Part 2 of 2)
 *
 * Utility methods demonstrating upper/lower bounded wildcards.
 * The PECS rule: Producer Extends, Consumer Super.
 *
 * Implement the 4 TODO methods.
 */
public class PortfolioUtils {

    /**
     * TODO 1 — Sum a list of any Number subtype.
     *
     * Uses upper-bounded wildcard: List<? extends Number>
     * This makes the method accept List<Integer>, List<Double>, List<BigDecimal>, etc.
     *
     * Implementation:
     *   Iterate and call n.doubleValue() on each element, summing into a double.
     *   Return as BigDecimal.valueOf(sum).
     *
     * Example:
     *   sumValues(List.of(1, 2, 3))            → 6.0 (from List<Integer>)
     *   sumValues(List.of(1.5, 2.5))           → 4.0 (from List<Double>)
     *
     * Why ? extends Number and not just Number?
     *   List<Integer> is NOT a List<Number> in Java (invariant generics).
     *   List<? extends Number> accepts both.
     */
    public BigDecimal sumValues(List<? extends Number> values) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Copy all elements from source into destination.
     *
     * Classic PECS example:
     *   - source is a Producer → ? extends T
     *   - dest is a Consumer   → ? super T
     *
     * This allows: copyAll(List<Integer> src, List<Number> dest) — works!
     *
     * Example:
     *   List<Integer> src  = List.of(1, 2, 3);
     *   List<Number>  dest = new ArrayList<>();
     *   copyAll(src, dest);  // dest now contains [1, 2, 3]
     */
    public <T> void copyAll(List<? extends T> source, List<? super T> dest) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Return the maximum element from a collection.
     *
     * T must extend Comparable<T> so we can call compareTo().
     *
     * Throw IllegalArgumentException if the collection is empty.
     *
     * Example:
     *   max(List.of(3, 1, 4, 1, 5, 9)) → 9
     *   max(List.of("banana", "apple", "cherry")) → "cherry"
     */
    public <T extends Comparable<T>> T max(Collection<T> items) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 4 — Swap two elements in a list by index (in-place).
     *
     * The wildcard-free signature <T> works here because we both
     * read AND write the same list.
     *
     * Throw IndexOutOfBoundsException if either index is out of range.
     *
     * Example:
     *   List<String> list = new ArrayList<>(List.of("a", "b", "c"));
     *   swap(list, 0, 2);   // list is now ["c", "b", "a"]
     */
    public <T> void swap(List<T> list, int i, int j) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
