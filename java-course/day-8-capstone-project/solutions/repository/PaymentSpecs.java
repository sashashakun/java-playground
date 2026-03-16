package com.example.fintech.capstone.repository;

import com.example.fintech.capstone.domain.Payment;
import com.example.fintech.capstone.domain.PaymentStatus;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;

// SOLUTION E — Repository: PaymentSpecs

public class PaymentSpecs {

    public static Specification<Payment> forAccount(String accountId) {   // TODO E1 ✓
        if (accountId == null || accountId.isBlank()) return null;
        return (root, query, cb) ->
            cb.equal(root.get("account").get("id"), accountId);
    }

    public static Specification<Payment> withStatus(PaymentStatus status) { // TODO E2 ✓
        if (status == null) return null;
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Payment> amountAtLeast(BigDecimal min) {   // TODO E3 ✓
        if (min == null) return null;
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("amount"), min);
    }

    public static Specification<Payment> createdBetween(Instant from, Instant to) { // TODO E4 ✓
        if (from == null && to == null) return null;
        return (root, query, cb) -> {
            if (from == null) return cb.lessThanOrEqualTo(root.get("createdAt"), to);
            if (to == null) return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
            return cb.between(root.get("createdAt"), from, to);
        };
    }
}
