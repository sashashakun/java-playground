package com.example.fintech.capstone.repository;

import com.example.fintech.capstone.domain.Payment;
import com.example.fintech.capstone.domain.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

/**
 * Capstone Exercise D — Repository: PaymentRepository
 *
 * TODO D1: Make PaymentRepository extend JpaRepository<Payment, String>
 *          AND JpaSpecificationExecutor<Payment>.
 *          Add a derived query: Page<Payment> findByAccountId(String accountId, Pageable pageable)
 *          Hint: Payment has `account` field (Account entity) — JPA traverses the relationship,
 *          so the derived method name is findByAccount_Id for nested field access.
 *
 * TODO D2: Add a @Query method:
 *          @Query("SELECT p FROM Payment p WHERE p.account.id = :accountId
 *                 AND p.status = :status AND p.createdAt BETWEEN :from AND :to")
 *          List<Payment> findByAccountAndStatusAndDateRange(
 *              @Param("accountId") String accountId,
 *              @Param("status") PaymentStatus status,
 *              @Param("from") Instant from,
 *              @Param("to") Instant to)
 */
public interface PaymentRepository // TODO D1: extends JpaRepository<Payment, String>, JpaSpecificationExecutor<Payment>
{
    // TODO D1: Page<Payment> findByAccount_Id(String accountId, Pageable pageable)

    // TODO D2: @Query(...) findByAccountAndStatusAndDateRange(...)
}
