package com.example.fintech.capstone.repository;

import com.example.fintech.capstone.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

// SOLUTION C — Repository: AccountRepository

public interface AccountRepository
    extends JpaRepository<Account, String>,             // TODO C1 ✓
            JpaSpecificationExecutor<Account> {         // TODO C1 ✓

    List<Account> findByOwnerId(String ownerId);                              // TODO C2 ✓

    Optional<Account> findByOwnerIdAndCurrency(String ownerId, String currency); // TODO C2 ✓
}
