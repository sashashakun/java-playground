package com.example.fintech.day7.specifications;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

// SOLUTION 06 — JPA Specifications: Repository

public interface TransactionRepository
    extends JpaRepository<Transaction, String>,         // TODO 1a ✓
            JpaSpecificationExecutor<Transaction> {     // TODO 1b ✓
}
