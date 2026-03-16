package com.example.fintech.day2.annotationsandpackages;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Exercise 07 — Annotations & Packages (Part 2 of 3)
 *
 * TODO: Define the @Idempotent annotation.
 *
 * Requirements:
 *   - Retention: RUNTIME
 *   - Target: METHOD only
 *   - Elements:
 *       String keyHeader() default "Idempotency-Key";  // HTTP header name for the key
 *       int ttlSeconds() default 86400;                // how long to retain the key (default 24h)
 *
 * Usage example:
 *   @Idempotent(keyHeader = "X-Idempotency-Key", ttlSeconds = 3600)
 *   public Payment createPayment(PaymentRequest request) { ... }
 */

// TODO: add @Retention and @Target, then define the @interface body
public @interface Idempotent {
    // TODO: add keyHeader() and ttlSeconds() elements with defaults
}
