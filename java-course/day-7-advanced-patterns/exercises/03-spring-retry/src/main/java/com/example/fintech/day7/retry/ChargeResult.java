package com.example.fintech.day7.retry;

import java.math.BigDecimal;

public record ChargeResult(
    String transactionId,
    BigDecimal amount,
    String currency,
    String status
) {
    public static ChargeResult success(String txId, BigDecimal amount, String currency) {
        return new ChargeResult(txId, amount, currency, "SUCCESS");
    }

    public static ChargeResult fallback(BigDecimal amount, String currency) {
        return new ChargeResult("FALLBACK-QUEUED", amount, currency, "QUEUED");
    }
}
