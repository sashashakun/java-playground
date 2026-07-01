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

// SOLUTION D — Repository: PaymentRepository

public interface PaymentRepository
    extends JpaRepository<Payment, String>,             // TODO D1 ✓
            JpaSpecificationExecutor<Payment> {         // TODO D1 ✓

    // TODO D1 ✓  — traverses account → id relationship
    Page<Payment> findByAccount_Id(String accountId, Pageable pageable);

    @Query("SELECT p FROM Payment p WHERE p.account.id = :accountId " +       // TODO D2 ✓
           "AND p.status = :status AND p.createdAt BETWEEN :from AND :to")
    List<Payment> findByAccountAndStatusAndDateRange(
        @Param("accountId") String accountId,
        @Param("status") PaymentStatus status,
        @Param("from") Instant from,
        @Param("to") Instant to);
}
