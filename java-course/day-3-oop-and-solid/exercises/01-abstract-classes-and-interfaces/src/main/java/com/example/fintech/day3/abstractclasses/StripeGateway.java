package com.example.fintech.day3.abstractclasses;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Exercise 01 — Concrete Gateway: Stripe
 *
 * Implement the 3 abstract methods from PaymentGateway and the 2 interface methods.
 *
 * Behavior spec:
 *
 *  authenticate():
 *    - If apiKey is null or blank → throw IllegalStateException("Stripe: missing API key")
 *    - Otherwise → do nothing (just "succeed")
 *
 *  submitCharge(request):
 *    - If request.currency() is "XYZ" → return ChargeResult(randomUUID, false, "Unsupported currency")
 *    - Otherwise → return ChargeResult(randomUUID, true, "Stripe charge accepted")
 *
 *  getGatewayName():
 *    - return "Stripe"
 *
 *  refund(transactionId, amount):
 *    - If amount.compareTo(BigDecimal.ZERO) <= 0 → throw IllegalArgumentException
 *    - Return RefundResult(true, "Stripe refund processed")
 *
 *  verify(transactionId):
 *    - Return true if the transactionId is in the internal set of processed transactions.
 *      Hint: store successfully charged transaction IDs in a Set<String> inside submitCharge.
 */
public class StripeGateway extends PaymentGateway implements Refundable, Verifiable {

    private final String apiKey;
    private final Set<String> processedTransactions = new HashSet<>();

    public StripeGateway(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected void authenticate() {
        // TODO: validate apiKey
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected ChargeResult submitCharge(ChargeRequest request) {
        // TODO: check currency, generate transactionId, track it
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getGatewayName() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public RefundResult refund(String transactionId, BigDecimal amount) {
        // TODO: validate amount > 0, return success
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean verify(String transactionId) {
        // TODO: return whether transactionId was processed
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
