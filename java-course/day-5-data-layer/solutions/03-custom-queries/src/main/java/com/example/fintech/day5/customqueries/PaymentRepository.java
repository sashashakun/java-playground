package com.example.fintech.day5.customqueries;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    @Query("SELECT p FROM Payment p WHERE p.amount.value BETWEEN :min AND :max")
    List<Payment> findByAmountRange(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    @Query("SELECT p FROM Payment p WHERE p.merchantId = :merchantId AND p.status = :status ORDER BY p.createdAt DESC")
    List<Payment> findByMerchantAndStatus(@Param("merchantId") String merchantId,
                                          @Param("status") PaymentStatus status);

    @Modifying
    @Query("UPDATE Payment p SET p.status = 'FAILED' WHERE p.status = 'PENDING' AND p.createdAt < :cutoff")
    int expireOldPendingPayments(@Param("cutoff") Instant cutoff);

    @Query("""
        SELECT p.id AS id,
               p.amount.value AS amountValue,
               p.amount.currency AS amountCurrency,
               p.merchantId AS merchantId,
               p.status AS status
        FROM Payment p WHERE p.merchantId = :merchantId
        """)
    List<PaymentSummary> findSummariesByMerchantId(@Param("merchantId") String merchantId);

    @Query(value = "SELECT currency, COUNT(*) AS cnt FROM payments GROUP BY currency",
           nativeQuery = true)
    List<Object[]> countByCurrency();
}
