package com.example.fintech.day7.validation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Exercise 05 — Custom Validation: Using the constraints
 *
 * @ValidCurrency on the field validates the currency string using CurrencyValidator.
 * @ValidAmount on the class validates the whole object using AmountValidator.
 *
 * Standard annotations (@NotBlank, @NotNull) work alongside custom ones.
 *
 * TODO 7: Add @ValidAmount at the class level (above the record declaration).
 * TODO 8: Add @ValidCurrency on the currency field (below @NotBlank).
 */
// TODO 7: @ValidAmount
public record PaymentRequest(
    @NotBlank String userId,
    @NotNull BigDecimal amount,
    @NotBlank
    // TODO 8: @ValidCurrency
    String currency
) {}
