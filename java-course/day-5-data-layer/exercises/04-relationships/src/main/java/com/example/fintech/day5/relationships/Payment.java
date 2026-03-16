package com.example.fintech.day5.relationships;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Exercise 04 — Relationships: Payment (child side)
 *
 * A Payment belongs to one Merchant. This is the "many" side of the bidirectional
 * @ManyToOne / @OneToMany relationship. The foreign key lives on this table.
 *
 * TypeScript analogy:
 *   In Prisma: model Payment { merchant Merchant @relation(fields: [merchantId]) }
 *
 * The entity already has @Entity, @Table, and the scalar fields annotated.
 * Your task is to add the relationship mapping.
 *
 * TODO 1: Add the @ManyToOne association to the `merchant` field:
 *         @ManyToOne(fetch = FetchType.LAZY)   ← lazy = don't load merchant unless accessed
 *
 * TODO 2: Add @JoinColumn(name = "merchant_id") to define the FK column name.
 *         (This replaces the old plain `merchantId` String field.)
 *
 * NOTE: The `merchantId` String field has been removed; the relationship replaces it.
 *       Use payment.getMerchant().getId() to get the merchant's ID.
 */
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

    // TODO 1: @ManyToOne(fetch = FetchType.LAZY)
    // TODO 2: @JoinColumn(name = "merchant_id")
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
