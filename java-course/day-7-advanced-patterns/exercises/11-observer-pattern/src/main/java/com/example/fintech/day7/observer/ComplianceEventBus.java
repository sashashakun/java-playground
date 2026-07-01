package com.example.fintech.day7.observer;

import java.util.ArrayList;
import java.util.List;

public class ComplianceEventBus {
    private final List<ComplianceListener> listeners = new ArrayList<>();

    public void subscribe(ComplianceListener listener) {
        listeners.add(listener);
    }

    public void publish(ComplianceEvent event) {
        for (ComplianceListener listener : listeners) {
            listener.onEvent(event);
        }
    }

    public int listenerCount() {
        return listeners.size();
    }
}
