package com.example.fintech.day7.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

// SOLUTION 01 — Spring Events: AuditListener

@Component
public class AuditListener {

    private final List<String> auditLog = new ArrayList<>();

    @EventListener                                          // TODO 1 ✓
    public void onCreated(PaymentEvents.PaymentCreatedEvent event) {
        auditLog.add("CREATED:" + event.paymentId() + ":" + event.userId());
    }

    @EventListener                                          // TODO 2 ✓
    public void onCompleted(PaymentEvents.PaymentCompletedEvent event) {
        auditLog.add("COMPLETED:" + event.paymentId() + ":" + event.amount());
    }

    @EventListener                                          // TODO 3 ✓
    public void onFailed(PaymentEvents.PaymentFailedEvent event) {
        auditLog.add("FAILED:" + event.paymentId() + ":" + event.reason());
    }

    public List<String> getAuditLog() {
        return List.copyOf(auditLog);
    }

    public void clear() {
        auditLog.clear();
    }
}
