package com.example.fintech.day2.annotationsandpackages;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AnnotationScanner {

    public record RateLimitedMethod(String methodName, int requestsPerMinute, String scope) {}

    public List<RateLimitedMethod> findRateLimitedMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
            .filter(m -> m.isAnnotationPresent(RateLimit.class))
            .map(m -> {
                RateLimit rl = m.getAnnotation(RateLimit.class);
                return new RateLimitedMethod(m.getName(), rl.requestsPerMinute(), rl.scope());
            })
            .toList();
    }

    public Optional<RateLimit> getRateLimit(Class<?> clazz, String methodName) {
        return Arrays.stream(clazz.getDeclaredMethods())
            .filter(m -> m.getName().equals(methodName))
            .findFirst()
            .map(m -> m.getAnnotation(RateLimit.class));
    }

    public boolean isIdempotent(Class<?> clazz, String methodName) {
        return Arrays.stream(clazz.getDeclaredMethods())
            .filter(m -> m.getName().equals(methodName))
            .findFirst()
            .map(m -> m.isAnnotationPresent(Idempotent.class))
            .orElse(false);
    }

    public int totalRateLimitBudget(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
            .filter(m -> m.isAnnotationPresent(RateLimit.class))
            .mapToInt(m -> m.getAnnotation(RateLimit.class).requestsPerMinute())
            .sum();
    }
}
