package com.example.fintech.capstone.web.dto;

import com.example.fintech.capstone.domain.Payment;
import com.example.fintech.capstone.domain.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentResponse(
    String id,
    String accountId,
    BigDecimal amount,
    String currency,
    String description,
    PaymentStatus status,
    Instant createdAt,
    Instant completedAt
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
            payment.getId(),
            payment.getAccount().getId(),
            payment.getAmount(),
            payment.getCurrency(),
            payment.getDescription(),
            payment.getStatus(),
            payment.getCreatedAt(),
            payment.getCompletedAt()
        );
    }
}
