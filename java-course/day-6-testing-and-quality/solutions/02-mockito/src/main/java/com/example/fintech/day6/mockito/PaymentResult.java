package com.example.fintech.day6.mockito;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResult(
    String transactionId,
    String userId,
    BigDecimal amount,
    String currency,
    String status,
    Instant processedAt
) {}
