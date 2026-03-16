package com.example.fintech.day6.webmvctest;

import java.math.BigDecimal;
import java.time.Instant;

public record TransferResponse(
    String transferId,
    String fromUserId,
    String toUserId,
    BigDecimal amount,
    String currency,
    String status,
    Instant createdAt
) {}
