package com.example.fintech.day7.events;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;

// SOLUTION 01 — Spring Events: NotificationListener

@Component
public class NotificationListener {

    private final List<String> notifications = new ArrayList<>();

    @TransactionalEventListener                                         // TODO 4 ✓
    // default phase is AFTER_COMMIT — fires only when transaction commits
    public void onCreated(PaymentEvents.PaymentCreatedEvent event) {
        notifications.add("SMS:payment-pending:" + event.paymentId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)  // TODO 5 ✓
    public void onFailed(PaymentEvents.PaymentFailedEvent event) {
        notifications.add("ALERT:payment-failed:" + event.paymentId() + ":" + event.reason());
    }

    public List<String> getNotifications() {
        return List.copyOf(notifications);
    }

    public void clear() {
        notifications.clear();
    }
}
