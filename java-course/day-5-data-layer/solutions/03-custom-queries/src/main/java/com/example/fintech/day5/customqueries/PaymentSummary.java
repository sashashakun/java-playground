package com.example.fintech.day5.customqueries;

import java.math.BigDecimal;

/**
 * Exercise 03 — Custom Queries: Projection
 *
 * A Spring Data interface-based projection.
 * When a repository method returns List<PaymentSummary>, Spring builds proxy objects
 * that only SELECT the columns you declared getters for — not the whole entity.
 *
 * TypeScript analogy: Prisma `select: { id: true, amount: true }` — only the fields
 * you specify are fetched from the database.
 *
 * This interface is already complete. Use it as the return type for TODO 4 in
 * PaymentRepository.
 */
public interface PaymentSummary {
    String getId();
    BigDecimal getAmountValue();    // maps to amount.value (embedded)
    String getAmountCurrency();     // maps to amount.currency (embedded)
    String getMerchantId();
    String getStatus();
}
