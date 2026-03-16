package com.example.fintech.capstone.service;

import com.example.fintech.capstone.domain.Account;
import com.example.fintech.capstone.exception.AccountNotFoundException;
import com.example.fintech.capstone.exception.InsufficientFundsException;
import com.example.fintech.capstone.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Capstone Exercise P — Unit Tests: AccountServiceTest
 *
 * TODO P1: Add @ExtendWith(MockitoExtension.class) to this class.
 *          Annotate accountRepository with @Mock.
 *          Annotate eventPublisher with @Mock.
 *          Annotate accountService with @InjectMocks.
 *
 * TODO P2: Complete createAccount_savesAndPublishesEvent:
 *          - when(accountRepository.save(any())).thenAnswer(inv -> inv.getArgument(0))
 *          - call accountService.createAccount("owner-1", "USD", new BigDecimal("500"))
 *          - verify accountRepository.save() was called
 *          - verify eventPublisher.publishEvent() was called
 *          - assertThat the returned account has ownerId "owner-1"
 *
 * TODO P3: Complete transfer_insufficientFunds_throws:
 *          - Create two Account stubs (fromAccount with balance 100, toAccount)
 *          - when(accountRepository.findById("from-1")).thenReturn(Optional.of(fromAccount))
 *          - when(accountRepository.findById("to-1")).thenReturn(Optional.of(toAccount))
 *          - assertThatThrownBy(() -> accountService.transfer("from-1", "to-1", new BigDecimal("500")))
 *              .isInstanceOf(InsufficientFundsException.class)
 */
// TODO P1: @ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    // TODO P1: @Mock
    AccountRepository accountRepository;

    // TODO P1: @Mock
    ApplicationEventPublisher eventPublisher;

    // TODO P1: @InjectMocks
    AccountService accountService;

    @Test
    // TODO P2: implement test body
    void createAccount_savesAndPublishesEvent() {
        // TODO P2: implement
    }

    @Test
    // TODO P3: implement test body
    void transfer_insufficientFunds_throws() {
        // TODO P3: implement
    }

    @Test
    void transfer_accountNotFound_throws() {
        when(accountRepository.findById("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.transfer("ghost", "to-1", BigDecimal.TEN))
            .isInstanceOf(AccountNotFoundException.class);
    }
}
