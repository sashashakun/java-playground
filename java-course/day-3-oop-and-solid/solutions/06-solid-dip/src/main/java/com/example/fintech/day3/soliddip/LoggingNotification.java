package com.example.fintech.day3.soliddip;

import java.util.ArrayList;
import java.util.List;

public class LoggingNotification implements NotificationPort {

    private final List<String> messages = new ArrayList<>();

    @Override
    public void notify(String eventType, Payment payment) {
        messages.add("[%s] Payment %s: %s %s"
            .formatted(eventType, payment.id(), payment.amount(), payment.currency()));
    }

    public List<String> getMessages() { return List.copyOf(messages); }
}
