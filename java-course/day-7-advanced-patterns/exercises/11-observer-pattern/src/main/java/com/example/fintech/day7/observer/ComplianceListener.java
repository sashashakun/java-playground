package com.example.fintech.day7.observer;

@FunctionalInterface
public interface ComplianceListener {
    void onEvent(ComplianceEvent event);
}
