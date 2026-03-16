package com.example.fintech.day3.patterns;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 07 — Observer Pattern: PaymentEventBus
 *
 * A simple synchronous event bus. Publishers call publish(); listeners react.
 *
 * Events (provided sealed hierarchy — no changes needed):
 */
public class PaymentEventBus {

    // ─── Event hierarchy ────────────────────────────────────────────────────

    public sealed interface PaymentEvent
        permits PaymentEventBus.PaymentInitiated,
                PaymentEventBus.PaymentCompleted,
                PaymentEventBus.PaymentFailed {}

    public record PaymentInitiated(String paymentId, BigDecimal amount, String currency)
        implements PaymentEvent {}

    public record PaymentCompleted(String paymentId, BigDecimal amount)
        implements PaymentEvent {}

    public record PaymentFailed(String paymentId, String reason)
        implements PaymentEvent {}

    // ─── Listener interface ──────────────────────────────────────────────────

    @FunctionalInterface
    public interface PaymentListener {
        void onEvent(PaymentEvent event);
    }

    // ─── TODOs ───────────────────────────────────────────────────────────────

    private final List<PaymentListener> listeners = new ArrayList<>();

    /**
     * TODO 1 — subscribe(PaymentListener listener):
     *   Add listener to the internal list.
     */
    public void subscribe(PaymentListener listener) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — unsubscribe(PaymentListener listener):
     *   Remove listener from the list (by reference equality).
     */
    public void unsubscribe(PaymentListener listener) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 3 — publish(PaymentEvent event):
     *   Notify ALL registered listeners by calling onEvent(event).
     *   Listeners are called in registration order.
     *   If a listener throws, catch the exception, log it (System.err),
     *   and continue notifying remaining listeners.
     */
    public void publish(PaymentEvent event) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int listenerCount() { return listeners.size(); }
}
