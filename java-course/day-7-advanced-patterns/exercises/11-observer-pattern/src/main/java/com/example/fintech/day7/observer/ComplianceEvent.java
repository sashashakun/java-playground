package com.example.fintech.day7.observer;

public sealed interface ComplianceEvent permits
        ComplianceEvent.TransactionCreated,
        ComplianceEvent.TransactionFlagged {

    record TransactionCreated(String txId, double amount) implements ComplianceEvent {}

    record TransactionFlagged(String txId, String reason) implements ComplianceEvent {}
}
