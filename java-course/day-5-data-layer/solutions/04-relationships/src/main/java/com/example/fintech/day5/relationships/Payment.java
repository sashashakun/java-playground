package com.example.fintech.day5.relationships;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private String id;

    @Embedded
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(unique = true)
    private String idempotencyKey;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant updatedAt;

    protected Payment() {}

    public Payment(Money amount, String description, String idempotencyKey) {
        this.amount = amount;
        this.description = description;
        this.idempotencyKey = idempotencyKey;
        this.status = PaymentStatus.PENDING;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public String getId()               { return id; }
    public Money getAmount()            { return amount; }
    public PaymentStatus getStatus()    { return status; }
    public Merchant getMerchant()       { return merchant; }
    public String getDescription()      { return description; }
    public String getIdempotencyKey()   { return idempotencyKey; }
    public Instant getCreatedAt()       { return createdAt; }
    public Instant getUpdatedAt()       { return updatedAt; }

    public void setMerchant(Merchant merchant) { this.merchant = merchant; }
    public void setStatus(PaymentStatus status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }
}
