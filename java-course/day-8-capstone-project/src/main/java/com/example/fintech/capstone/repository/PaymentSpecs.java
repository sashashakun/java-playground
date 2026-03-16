package com.example.fintech.capstone.repository;

import com.example.fintech.capstone.domain.Payment;
import com.example.fintech.capstone.domain.PaymentStatus;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Capstone Exercise E — Repository: JPA Specification factory
 *
 * Use CriteriaBuilder (cb) to build type-safe predicates.
 * Return null to mean "no filter" (Specification.where(null) = no restriction).
 *
 * TODO E1: forAccount(String accountId)
 *          Predicate: cb.equal(root.get("account").get("id"), accountId)
 *          Return null if accountId is blank.
 *
 * TODO E2: withStatus(PaymentStatus status)
 *          Predicate: cb.equal(root.get("status"), status)
 *          Return null if status is null.
 *
 * TODO E3: amountAtLeast(BigDecimal min)
 *          Predicate: cb.greaterThanOrEqualTo(root.get("amount"), min)
 *          Return null if min is null.
 *
 * TODO E4: createdBetween(Instant from, Instant to)
 *          Predicate: cb.between(root.get("createdAt"), from, to)
 *          Return null if both are null.
 */
public class PaymentSpecs {

    // TODO E1:
    public static Specification<Payment> forAccount(String accountId) {
        return null; // placeholder
    }

    // TODO E2:
    public static Specification<Payment> withStatus(PaymentStatus status) {
        return null; // placeholder
    }

    // TODO E3:
    public static Specification<Payment> amountAtLeast(BigDecimal min) {
        return null; // placeholder
    }

    // TODO E4:
    public static Specification<Payment> createdBetween(Instant from, Instant to) {
        return null; // placeholder
    }
}
