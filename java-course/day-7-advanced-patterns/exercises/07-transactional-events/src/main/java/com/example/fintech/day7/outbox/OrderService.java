package com.example.fintech.day7.outbox;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Exercise 07 — Transactional Events: Service (provided, do not modify)
 *
 * confirmOrder:
 *  1. Saves the Order in CONFIRMED state
 *  2. Saves an OutboxEvent row (PENDING) — same transaction
 *  3. Publishes OrderConfirmedEvent — picked up by OutboxProcessor AFTER COMMIT
 *
 * The key insight: if any step fails, the whole transaction rolls back —
 * no orphaned outbox events, no confirmed order without an event.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ApplicationEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository,
                        OutboxEventRepository outboxEventRepository,
                        ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.outboxEventRepository = outboxEventRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Order confirmOrder(String customerId, BigDecimal amount) {
        Order order = new Order(customerId, amount);
        order.confirm();
        orderRepository.save(order);

        // Persist outbox event in same transaction
        outboxEventRepository.save(
            new OutboxEvent(order.getId(), OutboxEvent.EventType.ORDER_CONFIRMED));

        // Publish domain event — listener fires AFTER this transaction commits
        eventPublisher.publishEvent(
            new OrderEvents.OrderConfirmedEvent(order.getId(), customerId));

        return order;
    }

    @Transactional
    public Order cancelOrder(String customerId, BigDecimal amount, String reason) {
        Order order = new Order(customerId, amount);
        order.cancel();
        orderRepository.save(order);

        outboxEventRepository.save(
            new OutboxEvent(order.getId(), OutboxEvent.EventType.ORDER_CANCELLED));

        eventPublisher.publishEvent(
            new OrderEvents.OrderCancelledEvent(order.getId(), customerId, reason));

        return order;
    }
}
