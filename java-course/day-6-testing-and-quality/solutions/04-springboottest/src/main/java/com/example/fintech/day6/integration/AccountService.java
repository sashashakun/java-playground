package com.example.fintech.day6.integration;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository repo;

    public AccountService(AccountRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Account openAccount(String ownerId, String currency, BigDecimal initialDeposit) {
        if (repo.findByOwnerId(ownerId).isPresent()) {
            throw new DuplicateAccountException("Account already exists for: " + ownerId);
        }
        return repo.save(new Account(ownerId, currency, initialDeposit));
    }

    @Transactional
    public void transfer(String fromOwnerId, String toOwnerId, BigDecimal amount) {
        Account from = repo.findByOwnerId(fromOwnerId)
            .orElseThrow(() -> new AccountNotFoundException(fromOwnerId));
        Account to = repo.findByOwnerId(toOwnerId)
            .orElseThrow(() -> new AccountNotFoundException(toOwnerId));
        from.debit(amount);
        to.credit(amount);
        repo.save(from);
        repo.save(to);
    }

    @Transactional(readOnly = true)
    public Account getByOwnerId(String ownerId) {
        return repo.findByOwnerId(ownerId)
            .orElseThrow(() -> new AccountNotFoundException(ownerId));
    }

    public static class AccountNotFoundException extends RuntimeException {
        public AccountNotFoundException(String ownerId) {
            super("Account not found: " + ownerId);
        }
    }

    public static class DuplicateAccountException extends RuntimeException {
        public DuplicateAccountException(String msg) { super(msg); }
    }
}
