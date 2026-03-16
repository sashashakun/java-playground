package com.example.fintech.day6.webmvctest;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TransferRequest(
    @NotBlank(message = "fromUserId is required")
    String fromUserId,

    @NotBlank(message = "toUserId is required")
    String toUserId,

    @NotNull
    @DecimalMin(value = "0.01", message = "amount must be positive")
    BigDecimal amount,

    @NotBlank
    @Pattern(regexp = "[A-Z]{3}", message = "currency must be 3 uppercase letters")
    String currency,

    String reference
) {}
