package com.example.fintech.capstone.service;

import com.example.fintech.capstone.domain.Account;
import com.example.fintech.capstone.domain.DomainEvents;
import com.example.fintech.capstone.exception.AccountNotFoundException;
import com.example.fintech.capstone.exception.CurrencyMismatchException;
import com.example.fintech.capstone.repository.AccountRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

// SOLUTION F — Service: AccountService

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher eventPublisher;

    public AccountService(AccountRepository accountRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.accountRepository = accountRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional                                                         // TODO F1 ✓
    public Account createAccount(String ownerId, String currency, BigDecimal initialBalance) {
        Account account = new Account(ownerId, currency, initialBalance);
        accountRepository.save(account);
        eventPublisher.publishEvent(                                        // TODO F1 ✓
            new DomainEvents.AccountCreated(account.getId(), ownerId, currency));
        return account;
    }

    @Transactional                                                         // TODO F2 ✓
    public void transfer(String fromAccountId, String toAccountId, BigDecimal amount) {
        Account from = accountRepository.findById(fromAccountId)
            .orElseThrow(() -> new AccountNotFoundException(fromAccountId));
        Account to = accountRepository.findById(toAccountId)
            .orElseThrow(() -> new AccountNotFoundException(toAccountId));

        if (!from.getCurrency().equals(to.getCurrency())) {
            throw new CurrencyMismatchException(from.getCurrency(), to.getCurrency());
        }

        from.debit(amount);
        to.credit(amount);

        accountRepository.save(from);
        accountRepository.save(to);

        eventPublisher.publishEvent(new DomainEvents.FundsTransferred(     // TODO F2 ✓
            fromAccountId, toAccountId, amount, from.getCurrency(), Instant.now()));
    }

    @Transactional(readOnly = true)                                        // TODO F3 ✓
    public List<Account> findByOwner(String ownerId) {
        return accountRepository.findByOwnerId(ownerId);
    }

    @Transactional(readOnly = true)                                        // TODO F3 ✓
    public BigDecimal getBalance(String accountId) {
        return accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId))
            .getBalance();
    }
}
