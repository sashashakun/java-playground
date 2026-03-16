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

/**
 * Capstone Exercise F — Service: AccountService
 *
 * TODO F1: Annotate createAccount with @Transactional.
 *          After saving, publish a DomainEvents.AccountCreated event via eventPublisher.
 *
 * TODO F2: Annotate transfer with @Transactional.
 *          Business logic (already written below) — just add the annotation and
 *          publish a DomainEvents.FundsTransferred event after debit+credit.
 *
 * TODO F3: Annotate findByOwner and getBalance with @Transactional(readOnly = true).
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher eventPublisher;

    public AccountService(AccountRepository accountRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.accountRepository = accountRepository;
        this.eventPublisher = eventPublisher;
    }

    // TODO F1: @Transactional — save account, publish AccountCreated event
    public Account createAccount(String ownerId, String currency, BigDecimal initialBalance) {
        Account account = new Account(ownerId, currency, initialBalance);
        accountRepository.save(account);
        // TODO F1: eventPublisher.publishEvent(new DomainEvents.AccountCreated(...))
        return account;
    }

    // TODO F2: @Transactional — add annotation, publish FundsTransferred event
    public void transfer(String fromAccountId, String toAccountId, BigDecimal amount) {
        Account from = accountRepository.findById(fromAccountId)
            .orElseThrow(() -> new AccountNotFoundException(fromAccountId));
        Account to = accountRepository.findById(toAccountId)
            .orElseThrow(() -> new AccountNotFoundException(toAccountId));

        if (!from.getCurrency().equals(to.getCurrency())) {
            throw new CurrencyMismatchException(from.getCurrency(), to.getCurrency());
        }

        from.debit(amount);   // throws InsufficientFundsException if balance < amount
        to.credit(amount);

        accountRepository.save(from);
        accountRepository.save(to);

        // TODO F2: eventPublisher.publishEvent(new DomainEvents.FundsTransferred(...))
    }

    // TODO F3: @Transactional(readOnly = true)
    public List<Account> findByOwner(String ownerId) {
        return accountRepository.findByOwnerId(ownerId);
    }

    // TODO F3: @Transactional(readOnly = true)
    public BigDecimal getBalance(String accountId) {
        return accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId))
            .getBalance();
    }
}
