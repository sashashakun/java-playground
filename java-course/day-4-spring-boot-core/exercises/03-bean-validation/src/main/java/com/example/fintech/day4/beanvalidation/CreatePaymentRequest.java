package com.example.fintech.day4.beanvalidation;

import java.math.BigDecimal;

/**
 * Exercise 03 — Bean Validation
 *
 * Add Jakarta validation annotations to enforce these rules:
 *
 * amount:
 *   - TODO 1: @NotNull (message = "Amount is required")
 *   - TODO 2: @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
 *
 * currency:
 *   - TODO 3: @NotBlank(message = "Currency is required")
 *   - TODO 4: @Pattern(regexp = "[A-Z]{3}", message = "Currency must be a 3-letter ISO code")
 *
 * description:
 *   - TODO 5: @NotBlank(message = "Description is required")
 *   - TODO 6: @Size(max = 200, message = "Description must not exceed 200 characters")
 *
 * idempotencyKey:
 *   - TODO 7: @NotBlank(message = "Idempotency key is required")
 *
 * Hint: import jakarta.validation.constraints.*;
 */
public record CreatePaymentRequest(
    // TODO: add @NotNull @DecimalMin
    BigDecimal amount,

    // TODO: add @NotBlank @Pattern
    String currency,

    // TODO: add @NotBlank @Size
    String description,

    // TODO: add @NotBlank
    String idempotencyKey
) {}
