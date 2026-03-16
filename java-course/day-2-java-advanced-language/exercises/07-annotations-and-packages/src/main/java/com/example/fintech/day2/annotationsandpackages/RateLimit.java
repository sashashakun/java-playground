package com.example.fintech.day2.annotationsandpackages;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Exercise 07 — Annotations & Packages (Part 1 of 3)
 *
 * TODO: Define the @RateLimit annotation.
 *
 * Requirements:
 *   - Retention: RUNTIME (so it can be read via reflection)
 *   - Target: METHOD only
 *   - Elements:
 *       int requestsPerMinute() default 60;
 *       String scope() default "global";  // e.g., "global", "per-user", "per-ip"
 *
 * Usage example (don't add this code, just understand it):
 *   @RateLimit(requestsPerMinute = 10, scope = "per-user")
 *   public void transferFunds(UUID from, UUID to, BigDecimal amount) { ... }
 */

// TODO: add @Retention and @Target annotations, then define the @interface body
public @interface RateLimit {
    // TODO: add requestsPerMinute() and scope() elements with defaults
}
