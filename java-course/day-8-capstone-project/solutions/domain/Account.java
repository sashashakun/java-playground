package com.example.fintech.capstone.domain;

import com.example.fintech.capstone.exception.InsufficientFundsException;
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

// SOLUTION A — Domain: Account entity

@Entity                                                 // TODO A1 ✓
@Table(name = "accounts")                              // TODO A1 ✓
public class Account {

    @Id                                                 // TODO A2 ✓
    @GeneratedValue(strategy = GenerationType.UUID)     // TODO A2 ✓
    private String id;

    @Column(nullable = false)                           // TODO A3 ✓
    private String ownerId;

    @Column(nullable = false, length = 3)              // TODO A3 ✓
    private String currency;

    @Column(nullable = false, precision = 19, scale = 4) // TODO A3 ✓
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)                        // TODO A3 ✓
    @Column(nullable = false)                           // TODO A3 ✓
    private AccountStatus status;

    @Column(nullable = false, updatable = false)        // TODO A3 ✓
    private Instant createdAt;

    protected Account() {}

    public Account(String ownerId, String currency, BigDecimal initialBalance) {
        this.ownerId = ownerId;
        this.currency = currency;
        this.balance = initialBalance;
        this.status = AccountStatus.ACTIVE;
        this.createdAt = Instant.now();
    }

    public void debit(BigDecimal amount) {              // TODO A4 ✓
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(id, balance, amount);
        }
        this.balance = this.balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {             // TODO A4 ✓
        this.balance = this.balance.add(amount);
    }

    public void suspend() { this.status = AccountStatus.SUSPENDED; }
    public void close() { this.status = AccountStatus.CLOSED; }

    public String getId() { return id; }
    public String getOwnerId() { return ownerId; }
    public String getCurrency() { return currency; }
    public BigDecimal getBalance() { return balance; }
    public AccountStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
