package com.example.fintech.day7.outbox;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 07 — Transactional Events: Outbox processor
 *
 * @TransactionalEventListener fires ONLY after the publishing transaction commits.
 * This is the correct phase for "fire and forget" side effects:
 *   - Updating the outbox record to DISPATCHED
 *   - Sending to external message broker
 *
 * Contrast with @EventListener:
 *   - @EventListener fires INSIDE the transaction (bad: event fires even on rollback)
 *   - @TransactionalEventListener fires AFTER commit (safe: event only fires on success)
 *
 * AFTER_ROLLBACK phase: useful for cleanup / compensating actions on failure.
 *
 * TypeScript analogy: event emitter triggered in the .finally() or .then() of
 * a database transaction commit promise.
 *
 * TODO 1: Annotate onOrderConfirmed with:
 *         @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
 *         This method should:
 *           - Find the outbox event with status PENDING for event.orderId()
 *           - Call markDispatched() on it
 *           - Save it back via outboxEventRepository
 *           - Add event.orderId() to dispatchedOrderIds list
 *
 * TODO 2: Annotate onOrderCancelled with:
 *         @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
 *         Same pattern as onOrderConfirmed.
 *
 * Note: These listener methods run AFTER the original transaction has committed.
 *       They run in a NEW transaction (use @Transactional here too, or rely on
 *       the default behaviour which is a new transaction per listener).
 */
@Component
public class OutboxProcessor {

    private final OutboxEventRepository outboxEventRepository;

    // Track dispatched order IDs so tests can verify
    private final List<String> dispatchedOrderIds = new ArrayList<>();

    public OutboxProcessor(OutboxEventRepository outboxEventRepository) {
        this.outboxEventRepository = outboxEventRepository;
    }

    // TODO 1: @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderConfirmed(OrderEvents.OrderConfirmedEvent event) {
        // TODO 1: find pending outbox event, markDispatched(), save, record orderId
    }

    // TODO 2: @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCancelled(OrderEvents.OrderCancelledEvent event) {
        // TODO 2: same pattern
    }

    public List<String> getDispatchedOrderIds() {
        return List.copyOf(dispatchedOrderIds);
    }
}
