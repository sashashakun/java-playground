package com.example.fintech.day7.retry;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

// SOLUTION 03 — Spring Retry

@Service
public class PaymentGatewayClient {

    @Retryable(                                             // TODO 1 ✓
        retryFor = GatewayException.class,
        maxAttempts = 3,
        backoff = @Backoff(delay = 100)
    )
    public ChargeResult charge(BigDecimal amount, String currency) {
        String txId = "txn-" + UUID.randomUUID().toString().substring(0, 8);
        return ChargeResult.success(txId, amount, currency);
    }

    @Recover                                                // TODO 2 ✓
    public ChargeResult chargeRecovery(GatewayException ex, BigDecimal amount, String currency) {
        return ChargeResult.fallback(amount, currency);
    }
}
