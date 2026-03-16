package com.example.fintech.day7.specifications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Exercise 06 — JPA Specifications: Repository
 *
 * JpaSpecificationExecutor<T> adds these methods:
 *   findAll(Specification<T> spec)
 *   findAll(Specification<T> spec, Pageable pageable)
 *   findOne(Specification<T> spec)
 *   count(Specification<T> spec)
 *   exists(Specification<T> spec)
 *
 * TypeScript analogy: Prisma where() / TypeORM FindOptionsWhere — building
 * dynamic queries without string concatenation.
 *
 * TODO 1: Make TransactionRepository extend both JpaRepository<Transaction, String>
 *         AND JpaSpecificationExecutor<Transaction>.
 */
public interface TransactionRepository // TODO 1: extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction>
{
}
