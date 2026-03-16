package com.example.fintech.day1.records;

import java.util.Optional;

import com.example.fintech.day1.records.PaymentEvent.PaymentFailed;
import com.example.fintech.day1.records.PaymentEvent.PaymentInitiated;
import com.example.fintech.day1.records.PaymentEvent.PaymentProcessing;
import com.example.fintech.day1.records.PaymentEvent.PaymentSettled;

/** Solution for Exercise 06 — PaymentEventProcessor using pattern matching switch */
public class PaymentEventProcessor {

    public String describe(PaymentEvent event) {
        return switch (event) {
            case PaymentInitiated e  ->
                "Payment %s initiated: %s from %s"
                    .formatted(e.paymentId(), e.amount().formatted(), e.senderId());
            case PaymentProcessing e ->
                "Payment %s processing via %s"
                    .formatted(e.paymentId(), e.gatewayRef());
            case PaymentSettled e    ->
                "Payment %s settled: %s at %s"
                    .formatted(e.paymentId(), e.amount().formatted(), e.settledAt());
            case PaymentFailed e     ->
                "Payment %s FAILED [%d]: %s"
                    .formatted(e.paymentId(), e.errorCode(), e.reason());
        };
    }

    public String extractPaymentId(PaymentEvent event) {
        return switch (event) {
            case PaymentInitiated e  -> e.paymentId();
            case PaymentProcessing e -> e.paymentId();
            case PaymentSettled e    -> e.paymentId();
            case PaymentFailed e     -> e.paymentId();
        };
    }

    public boolean isTerminal(PaymentEvent event) {
        return switch (event) {
            case PaymentSettled e, PaymentFailed e -> true;
            default -> false;
        };
    }

    public Optional<Money> getAmount(PaymentEvent event) {
        return switch (event) {
            case PaymentInitiated e -> Optional.of(e.amount());
            case PaymentSettled e   -> Optional.of(e.amount());
            default                 -> Optional.empty();
        };
    }
}
