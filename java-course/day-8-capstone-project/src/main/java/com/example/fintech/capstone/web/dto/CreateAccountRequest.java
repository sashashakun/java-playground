package com.example.fintech.capstone.web.dto;

import com.example.fintech.capstone.validation.ValidCurrency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateAccountRequest(
    @NotBlank String ownerId,
    @NotBlank @ValidCurrency String currency,
    @NotNull @DecimalMin("0.00") BigDecimal initialBalance
) {}
