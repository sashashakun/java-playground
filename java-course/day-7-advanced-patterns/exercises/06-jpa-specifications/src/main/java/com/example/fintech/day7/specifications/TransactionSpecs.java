package com.example.fintech.day7.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Exercise 06 — JPA Specifications: Factory methods
 *
 * Specification<T> is a functional interface:
 *   Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb)
 *
 * root      — like the FROM clause (access fields via root.get("fieldName"))
 * cb        — builds predicates: cb.equal, cb.greaterThan, cb.lessThan, cb.like, cb.and, cb.or, cb.in
 * query     — rarely needed (use for distinct, subqueries)
 *
 * Best practice: return null (or Specification.where(null)) to mean "no filter".
 *
 * TypeScript analogy: query builder callbacks in Knex / TypeORM QueryBuilder.
 *
 * TODO 2: Implement forMerchant — cb.equal(root.get("merchantId"), merchantId)
 *         Return null if merchantId is blank.
 *
 * TODO 3: Implement withStatus — cb.equal(root.get("status"), status)
 *         Return null if status is null.
 *
 * TODO 4: Implement amountBetween — cb.between(root.get("amount"), min, max)
 *         Return null if both are null.
 *
 * TODO 5: Implement createdAfter — cb.greaterThanOrEqualTo(root.get("createdAt"), from)
 *         Return null if from is null.
 *
 * TODO 6: Implement forCountry — cb.equal(root.get("country"), country)
 *         Return null if country is blank.
 *
 * Specifications can be combined with:
 *   Specification.where(spec1).and(spec2).and(spec3)
 */
public class TransactionSpecs {

    // TODO 2: implement
    public static Specification<Transaction> forMerchant(String merchantId) {
        return null; // placeholder
    }

    // TODO 3: implement
    public static Specification<Transaction> withStatus(TransactionStatus status) {
        return null; // placeholder
    }

    // TODO 4: implement
    public static Specification<Transaction> amountBetween(BigDecimal min, BigDecimal max) {
        return null; // placeholder
    }

    // TODO 5: implement
    public static Specification<Transaction> createdAfter(Instant from) {
        return null; // placeholder
    }

    // TODO 6: implement
    public static Specification<Transaction> forCountry(String country) {
        return null; // placeholder
    }
}
