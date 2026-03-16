package com.example.fintech.day7.outbox;

/**
 * Domain events for orders — published via ApplicationEventPublisher.
 */
public class OrderEvents {

    public record OrderConfirmedEvent(String orderId, String customerId) {}

    public record OrderCancelledEvent(String orderId, String customerId, String reason) {}
}
