package com.example.fintech.day7.retry;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Exercise 03 — Spring Retry
 *
 * The payment gateway is sometimes unavailable (503, network timeout).
 * @Retryable automatically retries the method on failure.
 * @Recover provides a fallback when all retries are exhausted.
 *
 * TypeScript analogy: the `retry` npm package wrapping an axios call,
 * but declarative and managed by Spring AOP.
 *
 * @EnableRetry (on Day7Application) activates the retry AOP interceptor.
 *
 * TODO 1: Add @Retryable to `charge`:
 *         - retryFor = {GatewayException.class}    ← only retry on this exception
 *         - maxAttempts = 3                        ← 1 first try + 2 retries
 *         - backoff = @Backoff(delay = 100)        ← wait 100ms between attempts
 *
 * TODO 2: Add @Recover to `chargeRecovery`.
 *         The @Recover method is called when all attempts are exhausted.
 *         Signature rule: first param is the exception, rest match the original method params.
 *         Here: (GatewayException ex, BigDecimal amount, String currency)
 *         Return type must match the original method.
 *
 * NOTE: @Retryable uses Spring AOP, so `charge` must be called from OUTSIDE the bean
 * (same proxy rule as @Transactional). Tests call it via the Spring-managed bean.
 */
@Service
public class PaymentGatewayClient {

    // TODO 1: @Retryable(retryFor = GatewayException.class, maxAttempts = 3, backoff = @Backoff(delay = 100))
    public ChargeResult charge(BigDecimal amount, String currency) {
        // This method body is called by tests/service; tests inject a spy
        // that throws GatewayException to simulate failures.
        String txId = "txn-" + UUID.randomUUID().toString().substring(0, 8);
        return ChargeResult.success(txId, amount, currency);
    }

    // TODO 2: @Recover
    public ChargeResult chargeRecovery(GatewayException ex, BigDecimal amount, String currency) {
        // Queue for later processing when gateway is down
        return ChargeResult.fallback(amount, currency);
    }
}
