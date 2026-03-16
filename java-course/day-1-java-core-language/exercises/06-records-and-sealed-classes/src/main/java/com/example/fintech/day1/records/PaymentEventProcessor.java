package com.example.fintech.day1.records;

import java.util.Optional;

import com.example.fintech.day1.records.PaymentEvent.PaymentFailed;
import com.example.fintech.day1.records.PaymentEvent.PaymentInitiated;
import com.example.fintech.day1.records.PaymentEvent.PaymentProcessing;
import com.example.fintech.day1.records.PaymentEvent.PaymentSettled;

/**
 * Exercise 06 — Records & Sealed Classes (Part 3 of 3)
 *
 * Process PaymentEvent instances using Java 21 pattern matching switch expressions.
 *
 * Pattern matching switch syntax:
 *   switch (event) {
 *       case PaymentInitiated e  -> ...  // e is already cast to PaymentInitiated
 *       case PaymentProcessing e -> ...
 *       case PaymentSettled e    -> ...
 *       case PaymentFailed e     -> ...
 *   }
 *
 * Key properties:
 *   - No 'break' needed (switch EXPRESSIONS use arrows)
 *   - No 'default' needed when ALL sealed subtypes are covered (compiler checks!)
 *   - 'e' is a new pattern variable — typed and ready to use
 *   - The switch is an EXPRESSION — it produces a value you can assign or return
 */
public class PaymentEventProcessor {

    /**
     * TODO 1 — Return a human-readable description of the event.
     *
     * Expected outputs:
     *   PaymentInitiated("pay-1", Money("50.00","USD"), "user-1")
     *     → "Payment pay-1 initiated: 50.00 USD from user-1"
     *
     *   PaymentProcessing("pay-1", "GW-REF-XYZ")
     *     → "Payment pay-1 processing via GW-REF-XYZ"
     *
     *   PaymentSettled("pay-1", Money("50.00","USD"), Instant.parse("2024-01-15T10:30:00Z"))
     *     → "Payment pay-1 settled: 50.00 USD at 2024-01-15T10:30:00Z"
     *
     *   PaymentFailed("pay-1", "Insufficient funds", 4001)
     *     → "Payment pay-1 FAILED [4001]: Insufficient funds"
     *
     * Java hint:
     *   return switch (event) {
     *       case PaymentInitiated e  -> "Payment %s initiated: %s from %s"
     *           .formatted(e.paymentId(), e.amount().formatted(), e.senderId());
     *       case PaymentProcessing e -> ...
     *       case PaymentSettled e    -> ...
     *       case PaymentFailed e     -> ...
     *   };
     */
    public String describe(PaymentEvent event) {
        // TODO: implement using a switch expression
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Extract the payment ID from any event type.
     *
     * All PaymentEvent subtypes have a paymentId field.
     *
     * Example:
     *   extractPaymentId(new PaymentFailed("pay-42", "Error", 500)) → "pay-42"
     *
     * Use a switch expression with pattern matching.
     */
    public String extractPaymentId(PaymentEvent event) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — Returns true if the event represents a terminal state.
     *
     * Terminal states are SETTLED and FAILED — the payment lifecycle is over.
     * Non-terminal: INITIATED and PROCESSING — the payment may still change.
     *
     * Examples:
     *   isTerminal(new PaymentSettled(...))  → true
     *   isTerminal(new PaymentFailed(...))   → true
     *   isTerminal(new PaymentInitiated(...))→ false
     *   isTerminal(new PaymentProcessing(...))→ false
     */
    public boolean isTerminal(PaymentEvent event) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 4 — Return the Money amount if the event carries one.
     *
     * Only PaymentInitiated and PaymentSettled carry a Money amount.
     * Return Optional.empty() for PaymentProcessing and PaymentFailed.
     *
     * Examples:
     *   getAmount(new PaymentInitiated("p1", new Money("50.00","USD"), "u1"))
     *     → Optional.of(Money("50.00","USD"))
     *
     *   getAmount(new PaymentFailed("p1", "Error", 500))
     *     → Optional.empty()
     */
    public Optional<Money> getAmount(PaymentEvent event) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
