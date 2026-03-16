package com.example.fintech.day6.integration;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(nullable = false, unique = true)
    private String ownerId;

    @Column(length = 3, nullable = false)
    private String currency;

    @Column(precision = 19, scale = 4, nullable = false)
    private BigDecimal balance;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Account() {}

    public Account(String ownerId, String currency, BigDecimal initialBalance) {
        this.ownerId = ownerId;
        this.currency = currency;
        this.balance = initialBalance;
        this.createdAt = Instant.now();
    }

    public String getId()           { return id; }
    public String getOwnerId()      { return ownerId; }
    public String getCurrency()     { return currency; }
    public BigDecimal getBalance()  { return balance; }
    public Instant getCreatedAt()   { return createdAt; }

    public void credit(BigDecimal amount)  { this.balance = this.balance.add(amount); }
    public void debit(BigDecimal amount) {
        if (balance.compareTo(amount) < 0)
            throw new IllegalStateException("Insufficient funds");
        this.balance = this.balance.subtract(amount);
    }
}
