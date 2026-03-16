package com.example.fintech.day6.archunit.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(nullable = false)
    private String customerId;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Order() {}

    public Order(String customerId, BigDecimal totalAmount) {
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.status = OrderStatus.PENDING;
        this.createdAt = Instant.now();
    }

    public void confirm() { this.status = OrderStatus.CONFIRMED; }
    public void cancel()  { this.status = OrderStatus.CANCELLED; }

    public String getId()              { return id; }
    public String getCustomerId()      { return customerId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public OrderStatus getStatus()     { return status; }
    public Instant getCreatedAt()      { return createdAt; }

    public enum OrderStatus { PENDING, CONFIRMED, CANCELLED }
}
