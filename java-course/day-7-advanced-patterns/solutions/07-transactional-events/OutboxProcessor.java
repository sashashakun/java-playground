package com.example.fintech.day7.outbox;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;

// SOLUTION 07 — Transactional Events: OutboxProcessor

@Component
public class OutboxProcessor {

    private final OutboxEventRepository outboxEventRepository;
    private final List<String> dispatchedOrderIds = new ArrayList<>();

    public OutboxProcessor(OutboxEventRepository outboxEventRepository) {
        this.outboxEventRepository = outboxEventRepository;
    }

    @Transactional                                                              // new tx for post-commit work
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)         // TODO 1 ✓
    public void onOrderConfirmed(OrderEvents.OrderConfirmedEvent event) {
        outboxEventRepository.findByAggregateId(event.orderId()).stream()
            .filter(e -> e.getStatus() == OutboxEvent.EventStatus.PENDING)
            .forEach(e -> {
                e.markDispatched();
                outboxEventRepository.save(e);
            });
        dispatchedOrderIds.add(event.orderId());
    }

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)         // TODO 2 ✓
    public void onOrderCancelled(OrderEvents.OrderCancelledEvent event) {
        outboxEventRepository.findByAggregateId(event.orderId()).stream()
            .filter(e -> e.getStatus() == OutboxEvent.EventStatus.PENDING)
            .forEach(e -> {
                e.markDispatched();
                outboxEventRepository.save(e);
            });
        dispatchedOrderIds.add(event.orderId());
    }

    public List<String> getDispatchedOrderIds() {
        return List.copyOf(dispatchedOrderIds);
    }
}
