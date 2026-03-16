package com.example.fintech.day7.events;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 01 — Spring Events: Notification Listener (Transactional)
 *
 * @TransactionalEventListener differs from @EventListener:
 *   - It only fires AFTER the transaction commits (by default, phase = AFTER_COMMIT)
 *   - If the transaction rolls back, the event is never delivered
 *   - This prevents sending notifications for payments that were rolled back
 *
 * TypeScript analogy: a Prisma afterCommit hook or a transactional outbox consumer.
 *
 * TODO 4: Add @TransactionalEventListener (default phase = AFTER_COMMIT) to `onCreated`.
 *         This should fire only after the transaction that published the event commits.
 *
 * TODO 5: Add @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
 *         to `onFailed`. This fires only if the publishing transaction rolls back.
 *
 * The `notifications` list is used in tests.
 */
@Component
public class NotificationListener {

    private final List<String> notifications = new ArrayList<>();

    // TODO 4: @TransactionalEventListener
    public void onCreated(PaymentEvents.PaymentCreatedEvent event) {
        notifications.add("NOTIFY_CREATED:" + event.paymentId());
    }

    // TODO 5: @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onFailed(PaymentEvents.PaymentFailedEvent event) {
        notifications.add("NOTIFY_FAILED:" + event.paymentId());
    }

    public List<String> getNotifications() {
        return List.copyOf(notifications);
    }

    public void clear() {
        notifications.clear();
    }
}
