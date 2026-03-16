package com.example.fintech.day5.entities;

import java.math.BigDecimal;

/**
 * Exercise 01 — JPA Entities: Value Object
 *
 * A Money value object that should be embedded inside Payment.
 * Because Money has no identity of its own (it's defined by its values),
 * it maps to columns directly on the owning entity's table — not a separate table.
 *
 * TypeScript analogy: like a Prisma `type` block embedded in a model.
 *
 * TODO 1: Annotate this class with @Embeddable so JPA knows it can be inlined
 *         into the owning entity's table.
 *
 * TODO 2: Annotate `value` with:
 *         @Column(name = "amount", precision = 19, scale = 4, nullable = false)
 *
 * TODO 3: Annotate `currency` with:
 *         @Column(name = "currency", length = 3, nullable = false)
 */
public class Money {

    private BigDecimal value;

    private String currency;

    // JPA requires a no-arg constructor on @Embeddable classes
    protected Money() {}

    public Money(BigDecimal value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    public BigDecimal getValue()   { return value; }
    public String getCurrency()    { return currency; }

    @Override
    public String toString() {
        return value + " " + currency;
    }
}
