package com.example.fintech.day7.specifications;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;

// SOLUTION 06 — JPA Specifications: Factory methods

public class TransactionSpecs {

    public static Specification<Transaction> forMerchant(String merchantId) {
        // TODO 2 ✓
        if (merchantId == null || merchantId.isBlank()) return null;
        return (root, query, cb) -> cb.equal(root.get("merchantId"), merchantId);
    }

    public static Specification<Transaction> withStatus(TransactionStatus status) {
        // TODO 3 ✓
        if (status == null) return null;
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Transaction> amountBetween(BigDecimal min, BigDecimal max) {
        // TODO 4 ✓
        if (min == null && max == null) return null;
        return (root, query, cb) -> {
            if (min == null) return cb.lessThanOrEqualTo(root.get("amount"), max);
            if (max == null) return cb.greaterThanOrEqualTo(root.get("amount"), min);
            return cb.between(root.get("amount"), min, max);
        };
    }

    public static Specification<Transaction> createdAfter(Instant from) {
        // TODO 5 ✓
        if (from == null) return null;
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), from);
    }

    public static Specification<Transaction> forCountry(String country) {
        // TODO 6 ✓
        if (country == null || country.isBlank()) return null;
        return (root, query, cb) -> cb.equal(root.get("country"), country);
    }
}
