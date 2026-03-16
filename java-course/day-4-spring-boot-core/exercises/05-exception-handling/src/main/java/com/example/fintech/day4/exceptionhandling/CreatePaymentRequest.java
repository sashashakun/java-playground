package com.example.fintech.day4.exceptionhandling;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record CreatePaymentRequest(
    @NotNull @DecimalMin("0.01") BigDecimal amount,
    @NotBlank @Pattern(regexp = "[A-Z]{3}") String currency,
    @NotBlank String description,
    @NotBlank String idempotencyKey
) {}
