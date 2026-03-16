package com.example.fintech.day2.annotationsandpackages;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * Exercise 07 — Annotations & Packages (Part 3 of 3)
 *
 * Scan a class's methods for @RateLimit and @Idempotent annotations
 * using Java reflection.
 *
 * Implement the 4 TODO methods.
 *
 * Key reflection API:
 *   Class<?> clazz = SomeClass.class;
 *   Method[] methods = clazz.getDeclaredMethods();  // all methods (incl. private)
 *   Method m = clazz.getMethod("methodName", ParamType.class);
 *
 *   // Get annotation (returns null if absent):
 *   RateLimit rl = method.getAnnotation(RateLimit.class);
 *
 *   // Check if annotation is present:
 *   method.isAnnotationPresent(RateLimit.class)
 */
public class AnnotationScanner {

    /** Describes a discovered rate-limited method. */
    public record RateLimitedMethod(String methodName, int requestsPerMinute, String scope) {}

    /**
     * TODO 1 — Find all methods in a class annotated with @RateLimit.
     *
     * Steps:
     *   1. Get all declared methods: clazz.getDeclaredMethods()
     *   2. Filter those with @RateLimit present
     *   3. For each, create a RateLimitedMethod record
     *   4. Return as a List (order is not guaranteed by reflection)
     */
    public List<RateLimitedMethod> findRateLimitedMethods(Class<?> clazz) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Get the @RateLimit annotation on a specific method by name.
     *
     * Return Optional.empty() if the method doesn't exist or has no @RateLimit.
     *
     * Steps:
     *   1. Iterate clazz.getDeclaredMethods() to find by name
     *   2. Return Optional.ofNullable(method.getAnnotation(RateLimit.class))
     *
     * Note: use getDeclaredMethods() (not getMethods()) to include private methods.
     * If multiple methods have the same name (overloads), return the first one found.
     */
    public Optional<RateLimit> getRateLimit(Class<?> clazz, String methodName) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Check if a specific method is annotated with @Idempotent.
     *
     * Return false if the method doesn't exist or has no @Idempotent.
     */
    public boolean isIdempotent(Class<?> clazz, String methodName) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 4 — Get the total "requests per minute" budget across ALL rate-limited methods.
     *
     * Sum the requestsPerMinute values of all @RateLimit-annotated methods.
     * Return 0 if no methods are rate-limited.
     */
    public int totalRateLimitBudget(Class<?> clazz) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
