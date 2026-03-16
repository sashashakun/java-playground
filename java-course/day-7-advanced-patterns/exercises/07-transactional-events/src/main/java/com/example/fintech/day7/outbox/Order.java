package com.example.fintech.day7.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "orders")
public class Order {

    public enum Status { PENDING, CONFIRMED, CANCELLED }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private Instant createdAt;

    protected Order() {}

    public Order(String customerId, BigDecimal amount) {
        this.customerId = customerId;
        this.amount = amount;
        this.status = Status.PENDING;
        this.createdAt = Instant.now();
    }

    public void confirm() { this.status = Status.CONFIRMED; }
    public void cancel() { this.status = Status.CANCELLED; }

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public BigDecimal getAmount() { return amount; }
    public Status getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
