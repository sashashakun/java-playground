package com.example.fintech.capstone.service;

import com.example.fintech.capstone.domain.Account;
import com.example.fintech.capstone.domain.DomainEvents;
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

// SOLUTION P — Unit Tests: AccountServiceTest

@ExtendWith(MockitoExtension.class)                     // TODO P1 ✓
class AccountServiceTest {

    @Mock                                               // TODO P1 ✓
    AccountRepository accountRepository;

    @Mock                                               // TODO P1 ✓
    ApplicationEventPublisher eventPublisher;

    @InjectMocks                                        // TODO P1 ✓
    AccountService accountService;

    @Test
    void createAccount_savesAndPublishesEvent() {       // TODO P2 ✓
        when(accountRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Account result = accountService.createAccount("owner-1", "USD", new BigDecimal("500"));

        verify(accountRepository).save(any(Account.class));
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue()).isInstanceOf(DomainEvents.AccountCreated.class);
        assertThat(result.getOwnerId()).isEqualTo("owner-1");
        assertThat(result.getCurrency()).isEqualTo("USD");
    }

    @Test
    void transfer_insufficientFunds_throws() {          // TODO P3 ✓
        Account fromAccount = new Account("owner-1", "USD", new BigDecimal("100"));
        Account toAccount = new Account("owner-2", "USD", BigDecimal.ZERO);

        when(accountRepository.findById("from-1")).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById("to-1")).thenReturn(Optional.of(toAccount));

        assertThatThrownBy(() ->
            accountService.transfer("from-1", "to-1", new BigDecimal("500")))
            .isInstanceOf(InsufficientFundsException.class)
            .hasMessageContaining("Insufficient funds");
    }

    @Test
    void transfer_accountNotFound_throws() {
        when(accountRepository.findById("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.transfer("ghost", "to-1", BigDecimal.TEN))
            .isInstanceOf(AccountNotFoundException.class);
    }
}
