package com.example.fintech.day7.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

/**
 * Transactional Outbox pattern: events are persisted in the same DB transaction
 * as the business entity. A listener then picks them up after commit to dispatch
 * to an external system (e.g. Kafka, SQS).
 *
 * This prevents the "dual-write" problem: if publishing to a message broker
 * fails, the outbox row is still in the DB and can be retried.
 */
@Entity
@Table(name = "outbox_events")
public class OutboxEvent {

    public enum EventType { ORDER_CONFIRMED, ORDER_CANCELLED }
    public enum EventStatus { PENDING, DISPATCHED, FAILED }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String aggregateId; // the Order id

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant processedAt;

    protected OutboxEvent() {}

    public OutboxEvent(String aggregateId, EventType eventType) {
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.status = EventStatus.PENDING;
        this.createdAt = Instant.now();
    }

    public void markDispatched() {
        this.status = EventStatus.DISPATCHED;
        this.processedAt = Instant.now();
    }

    public void markFailed() {
        this.status = EventStatus.FAILED;
        this.processedAt = Instant.now();
    }

    public String getId() { return id; }
    public String getAggregateId() { return aggregateId; }
    public EventType getEventType() { return eventType; }
    public EventStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getProcessedAt() { return processedAt; }
}
