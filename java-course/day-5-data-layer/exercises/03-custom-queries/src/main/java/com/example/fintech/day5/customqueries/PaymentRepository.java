package com.example.fintech.day5.customqueries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * Exercise 03 — Custom Queries
 *
 * When derived method names get unwieldy, use @Query with JPQL (object-oriented SQL)
 * or native SQL. JPQL references entity class names and field names, not table/column names.
 *
 * TypeScript analogy: Prisma's $queryRaw or TypeORM's @Query decorator on a custom method.
 *
 * TODO 1: Write a JPQL @Query that finds all payments whose amount value is between
 *         `minAmount` and `maxAmount` (inclusive).
 *         - JPQL: SELECT p FROM Payment p WHERE p.amount.value BETWEEN :min AND :max
 *         - Use @Param("min") and @Param("max") to bind named parameters.
 *         - Method signature: findByAmountRange(BigDecimal min, BigDecimal max)
 *         - Return type: List<Payment>
 *
 * TODO 2: Write a JPQL @Query that finds all payments for a given merchant and status.
 *         - JPQL: SELECT p FROM Payment p WHERE p.merchantId = :merchantId
 *                 AND p.status = :status ORDER BY p.createdAt DESC
 *         - Method: findByMerchantAndStatus(String merchantId, PaymentStatus status)
 *         - Return type: List<Payment>
 *
 * TODO 3: Write a @Modifying @Query that bulk-updates all PENDING payments
 *         older than a given number of seconds to FAILED.
 *         - JPQL: UPDATE Payment p SET p.status = 'FAILED'
 *                 WHERE p.status = 'PENDING' AND p.createdAt < :cutoff
 *         - Use @Modifying to signal a DML statement.
 *         - Method: expireOldPendingPayments(java.time.Instant cutoff)
 *         - Return type: int (number of rows updated)
 *         NOTE: @Modifying queries must run inside a transaction. Tests will
 *               use @Transactional on the test method.
 *
 * TODO 4: Write a JPQL @Query returning a List<PaymentSummary> projection
 *         for a given merchant (uses the PaymentSummary interface).
 *         - JPQL: SELECT p.id AS id, p.amount.value AS amountValue,
 *                        p.amount.currency AS amountCurrency,
 *                        p.merchantId AS merchantId, p.status AS status
 *                 FROM Payment p WHERE p.merchantId = :merchantId
 *         - Method: findSummariesByMerchantId(String merchantId)
 *         - Return type: List<PaymentSummary>
 *
 * TODO 5: Write a native SQL @Query (nativeQuery = true) that counts payments
 *         grouped by currency, returning Object[] rows of [currency, count].
 *         - SQL: SELECT currency, COUNT(*) AS cnt FROM payments GROUP BY currency
 *         - Method: countByCurrency()
 *         - Return type: List<Object[]>
 */
public interface PaymentRepository extends JpaRepository<Payment, String> {

    // TODO 1
    // TODO 2
    // TODO 3
    // TODO 4
    // TODO 5
}
