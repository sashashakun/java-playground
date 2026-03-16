package com.example.fintech.day5.pagination;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

/**
 * Exercise 06 — Pagination & Sorting
 *
 * Pagination is critical for fintech APIs: you never want to return 10 million
 * transactions in one HTTP response.
 *
 * Spring Data supports two pagination abstractions:
 *   - Page<T>  : includes total count (runs a COUNT(*) query). Use for UIs with page numbers.
 *   - Slice<T> : only knows if there's a next page. Cheaper (no COUNT). Use for infinite scroll.
 *
 * TypeScript analogy:
 *   Prisma: findMany({ skip, take, orderBy })  → manual pagination
 *   Spring: pass a Pageable object → automatic LIMIT / OFFSET + optional COUNT
 *
 * TODO 1: Add a paginated method that returns a Page<Payment> filtered by status.
 *         Spring Data sees the Pageable parameter and adds LIMIT/OFFSET automatically.
 *         Method: findByStatus(PaymentStatus status, Pageable pageable) → Page<Payment>
 *
 * TODO 2: Add a method that returns a Slice<Payment> for a given merchant.
 *         Slice does NOT count total — faster for "load more" style UIs.
 *         Method: findByMerchantId(String merchantId, Pageable pageable) → Slice<Payment>
 *
 * TODO 3: Add a @Query that returns a Page<Payment> for payments above a threshold,
 *         ordered inside the query itself.
 *         JPQL: SELECT p FROM Payment p WHERE p.amount.value > :threshold ORDER BY p.amount.value DESC
 *         Note: when using ORDER BY inside @Query with Pageable, also supply a
 *               countQuery attribute:
 *               countQuery = "SELECT count(p) FROM Payment p WHERE p.amount.value > :threshold"
 *         Method: findHighValuePayments(BigDecimal threshold, Pageable pageable) → Page<Payment>
 */
public interface PaymentRepository extends JpaRepository<Payment, String> {

    // TODO 1

    // TODO 2

    // TODO 3

}
