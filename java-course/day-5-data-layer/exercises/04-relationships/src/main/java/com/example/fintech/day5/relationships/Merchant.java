package com.example.fintech.day5.relationships;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 04 — Relationships: Merchant (parent side)
 *
 * A Merchant owns many Payments. This is the "one" side of the @OneToMany relationship.
 *
 * TypeScript analogy:
 *   In Prisma: model Merchant { payments Payment[] }
 *   The `payments` field holds the collection.
 *
 * TODO 1: Add @Entity to declare this as a JPA-managed entity.
 *
 * TODO 2: Add @Table(name = "merchants")
 *
 * TODO 3: Annotate `id` with @Id and @GeneratedValue(strategy = GenerationType.UUID)
 *         and @Column(updatable = false, nullable = false)
 *
 * TODO 4: Annotate `name` with @Column(nullable = false)
 *
 * TODO 5: Declare the one-to-many relationship with:
 *         @OneToMany(
 *             mappedBy = "merchant",         ← matches the field name in Payment
 *             cascade  = CascadeType.ALL,    ← save merchant → saves its payments
 *             orphanRemoval = true,          ← remove payment from list → deletes it
 *             fetch = FetchType.LAZY         ← don't load all payments unless asked
 *         )
 *
 * HELPER METHODS (already implemented):
 *   addPayment(Payment p)    — keeps both sides of the bidirectional link in sync
 *   removePayment(Payment p) — removes and clears back-reference
 */
public class Merchant {

    private String id;

    private String name;

    private String country;

    private List<Payment> payments = new ArrayList<>();

    protected Merchant() {}

    public Merchant(String name, String country) {
        this.name = name;
        this.country = country;
    }

    /** Keeps both sides of the bidirectional association in sync. */
    public void addPayment(Payment payment) {
        payments.add(payment);
        payment.setMerchant(this);
    }

    public void removePayment(Payment payment) {
        payments.remove(payment);
        payment.setMerchant(null);
    }

    public String getId()           { return id; }
    public String getName()         { return name; }
    public String getCountry()      { return country; }
    public List<Payment> getPayments() { return payments; }
}
