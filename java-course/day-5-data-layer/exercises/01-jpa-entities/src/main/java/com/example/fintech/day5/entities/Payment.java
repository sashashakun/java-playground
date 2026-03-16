package com.example.fintech.day5.entities;

import java.time.Instant;

/**
 * Exercise 01 — JPA Entities: Payment
 *
 * Make this plain Java class a proper JPA entity by adding the missing annotations.
 * Each TODO tells you which annotation to apply and why.
 *
 * TypeScript analogy: annotating a class to match a Prisma model definition.
 *
 * TODO 1: Add @Entity to declare this as a JPA-managed entity class.
 *
 * TODO 2: Add @Table(name = "payments") to control the table name and add
 *         index hints:
 *           indexes = {
 *             @Index(name = "idx_payment_status",   columnList = "status"),
 *             @Index(name = "idx_payment_merchant", columnList = "merchant_id")
 *           }
 *
 * TODO 3: Annotate `id` with:
 *           @Id
 *           @GeneratedValue(strategy = GenerationType.UUID)
 *           @Column(updatable = false, nullable = false)
 *
 * TODO 4: Annotate `amount` with @Embedded (links to the @Embeddable Money class).
 *
 * TODO 5: Annotate `status` with:
 *           @Enumerated(EnumType.STRING)   ← stores "PENDING" not 0
 *           @Column(nullable = false)
 *
 * TODO 6: Annotate `merchantId` with @Column(name = "merchant_id", nullable = false)
 *
 * TODO 7: Annotate `description` with @Column(nullable = false, length = 200)
 *
 * TODO 8: Annotate `idempotencyKey` with @Column(unique = true)
 *
 * TODO 9: Annotate `createdAt` with @Column(nullable = false, updatable = false)
 */
public class Payment {

    private String id;

    private Money amount;

    private PaymentStatus status;

    private String merchantId;

    private String description;

    private String idempotencyKey;

    private Instant createdAt;

    private Instant updatedAt;

    // JPA requires a public or protected no-arg constructor
    protected Payment() {}

    public Payment(Money amount, String merchantId, String description, String idempotencyKey) {
        this.amount = amount;
        this.merchantId = merchantId;
        this.description = description;
        this.idempotencyKey = idempotencyKey;
        this.status = PaymentStatus.PENDING;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public String getId()               { return id; }
    public Money getAmount()            { return amount; }
    public PaymentStatus getStatus()    { return status; }
    public String getMerchantId()       { return merchantId; }
    public String getDescription()      { return description; }
    public String getIdempotencyKey()   { return idempotencyKey; }
    public Instant getCreatedAt()       { return createdAt; }
    public Instant getUpdatedAt()       { return updatedAt; }

    public void setStatus(PaymentStatus status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }
}
