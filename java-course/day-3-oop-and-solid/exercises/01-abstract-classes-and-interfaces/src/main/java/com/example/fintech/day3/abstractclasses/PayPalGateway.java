package com.example.fintech.day3.abstractclasses;

import java.util.UUID;

/**
 * Exercise 01 — Concrete Gateway: PayPal
 *
 * PayPal supports charges and verification but NOT refunds
 * (we pretend refunds go through a separate PayPal dispute portal).
 *
 * Behavior spec:
 *
 *  authenticate():
 *    - If clientId OR clientSecret is null/blank → throw IllegalStateException("PayPal: missing credentials")
 *    - Otherwise succeed silently
 *
 *  submitCharge(request):
 *    - If amount > 10_000 (the PayPal limit in this simulation) →
 *        return ChargeResult(randomUUID, false, "PayPal: amount exceeds limit")
 *    - Otherwise → return ChargeResult(randomUUID, true, "PayPal charge accepted")
 *      Track the transactionId in a Set<String>.
 *
 *  getGatewayName():
 *    - return "PayPal"
 *
 *  verify(transactionId):
 *    - Return true if the transactionId was previously charged successfully.
 *
 * NOTE: PayPalGateway does NOT implement Refundable.
 * If client code casts to Refundable, it gets a ClassCastException — that's intentional.
 * This motivates Exercise 05 (ISP): clients should depend on Refundable, not on concrete types.
 */
public class PayPalGateway extends PaymentGateway implements Verifiable {

    private final String clientId;
    private final String clientSecret;
    private final java.util.Set<String> processedTransactions = new java.util.HashSet<>();

    public PayPalGateway(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    protected void authenticate() {
        // TODO: validate both clientId and clientSecret are non-blank
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected ChargeResult submitCharge(ChargeRequest request) {
        // TODO: check amount limit (> 10_000), track transactionId on success
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getGatewayName() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean verify(String transactionId) {
        // TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
