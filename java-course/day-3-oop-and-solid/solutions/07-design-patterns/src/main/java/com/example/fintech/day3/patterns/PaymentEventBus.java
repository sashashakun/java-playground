package com.example.fintech.day3.patterns;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PaymentEventBus {

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

    @FunctionalInterface
    public interface PaymentListener {
        void onEvent(PaymentEvent event);
    }

    private final List<PaymentListener> listeners = new ArrayList<>();

    public void subscribe(PaymentListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(PaymentListener listener) {
        listeners.remove(listener);
    }

    public void publish(PaymentEvent event) {
        for (PaymentListener listener : List.copyOf(listeners)) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                System.err.println("Listener threw exception: " + e.getMessage());
            }
        }
    }

    public int listenerCount() { return listeners.size(); }
}
