package com.example.fintech.capstone.web.dto;

import com.example.fintech.capstone.validation.ValidCurrency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreatePaymentRequest(
    @NotBlank String accountId,
    @NotNull @DecimalMin("0.01") BigDecimal amount,
    @NotBlank @ValidCurrency String currency,
    @NotBlank String description
) {}
