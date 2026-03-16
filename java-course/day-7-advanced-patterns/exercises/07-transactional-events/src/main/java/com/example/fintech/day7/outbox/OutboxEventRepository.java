package com.example.fintech.day7.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, String> {

    List<OutboxEvent> findByStatus(OutboxEvent.EventStatus status);

    List<OutboxEvent> findByAggregateId(String aggregateId);
}
