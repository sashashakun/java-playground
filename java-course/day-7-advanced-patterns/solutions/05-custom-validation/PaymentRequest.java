package com.example.fintech.day7.validation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

// SOLUTION 05 — Custom Validation: PaymentRequest with constraints applied

@ValidAmount                                            // TODO 7 ✓
public record PaymentRequest(
    @NotBlank String userId,
    @NotNull BigDecimal amount,
    @NotBlank
    @ValidCurrency                                      // TODO 8 ✓
    String currency
) {}
