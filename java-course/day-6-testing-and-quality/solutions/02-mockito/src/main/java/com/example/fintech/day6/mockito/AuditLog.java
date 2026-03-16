package com.example.fintech.day6.mockito;

/** Records every significant event for compliance / audit. */
public interface AuditLog {
    void record(String eventType, String actorId, String details);
}
