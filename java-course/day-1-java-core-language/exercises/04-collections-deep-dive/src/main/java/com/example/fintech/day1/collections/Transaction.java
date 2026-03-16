package com.example.fintech.day1.collections;

/**
 * A simple transaction record — immutable value object.
 *
 * Java record auto-generates:
 *   - Constructor: new Transaction(id, amountCents, currency, type, description)
 *   - Getters: tx.id(), tx.amountCents(), tx.currency(), tx.type(), tx.description()
 *   - equals() and hashCode() based on all fields
 *   - toString()
 *
 * No TODO here — this is provided. Just understand the record syntax.
 */
public record Transaction(
    String id,
    long amountCents,
    String currency,
    String type,
    String description
) {}
