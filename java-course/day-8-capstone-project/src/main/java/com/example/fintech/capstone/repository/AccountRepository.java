package com.example.fintech.capstone.repository;

import com.example.fintech.capstone.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * Capstone Exercise C — Repository: AccountRepository
 *
 * TODO C1: Make AccountRepository extend both:
 *          JpaRepository<Account, String>           ← standard CRUD + paging
 *          JpaSpecificationExecutor<Account>        ← dynamic Specification queries
 *
 * TODO C2: Add derived query methods:
 *          List<Account> findByOwnerId(String ownerId)
 *          Optional<Account> findByOwnerIdAndCurrency(String ownerId, String currency)
 */
public interface AccountRepository // TODO C1: extends JpaRepository<Account, String>, JpaSpecificationExecutor<Account>
{
    // TODO C2: findByOwnerId(String ownerId)
    // TODO C2: findByOwnerIdAndCurrency(String ownerId, String currency)
}
