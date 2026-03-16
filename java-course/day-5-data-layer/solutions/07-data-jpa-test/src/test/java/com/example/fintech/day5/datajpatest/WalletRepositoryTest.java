package com.example.fintech.day5.datajpatest;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class WalletRepositoryTest {

    @Autowired
    private WalletRepository repo;

    @Test
    void findByOwnerId_returnsOnlyThatOwnersWallets() {
        repo.save(new Wallet("alice", "USD", new BigDecimal("100")));
        repo.save(new Wallet("alice", "EUR", new BigDecimal("200")));
        repo.save(new Wallet("bob",   "USD", new BigDecimal("50")));

        List<Wallet> aliceWallets = repo.findByOwnerId("alice");
        assertThat(aliceWallets).hasSize(2);
        assertThat(repo.findByOwnerId("charlie")).isEmpty();
    }

    @Test
    void findByOwnerIdAndStatus_filtersCorrectly() {
        Wallet active = repo.save(new Wallet("dave", "USD", new BigDecimal("500")));
        Wallet frozen = repo.save(new Wallet("dave", "EUR", new BigDecimal("100")));
        frozen.freeze();
        repo.save(frozen);

        List<Wallet> activeWallets = repo.findByOwnerIdAndStatus("dave", WalletStatus.ACTIVE);
        assertThat(activeWallets).hasSize(1);
        assertThat(activeWallets.get(0).getId()).isEqualTo(active.getId());

        assertThat(repo.findByOwnerIdAndStatus("dave", WalletStatus.CLOSED)).isEmpty();
    }

    @Test
    void existsByOwnerIdAndCurrency_detectsDuplicates() {
        repo.save(new Wallet("eve", "USD", new BigDecimal("1000")));

        assertThat(repo.existsByOwnerIdAndCurrency("eve", "USD")).isTrue();
        assertThat(repo.existsByOwnerIdAndCurrency("eve", "EUR")).isFalse();
    }

    @Test
    void findHighBalanceActiveWallets_excludesFrozenAndLowBalance() {
        repo.save(new Wallet("user1", "USD", new BigDecimal("100")));
        repo.save(new Wallet("user2", "USD", new BigDecimal("500")));
        repo.save(new Wallet("user3", "USD", new BigDecimal("1000")));

        Wallet frozen = repo.save(new Wallet("user4", "USD", new BigDecimal("2000")));
        frozen.freeze();
        repo.save(frozen);

        List<Wallet> result = repo.findHighBalanceActiveWallets(new BigDecimal("400"));

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(w -> w.getBalance().compareTo(new BigDecimal("400")) > 0);
        assertThat(result).allMatch(w -> w.getStatus() == WalletStatus.ACTIVE);
    }

    @Test
    void findByOwnerIdOrderedByBalance_returnsPaginatedDescOrder() {
        repo.save(new Wallet("frank", "USD", new BigDecimal("50")));
        repo.save(new Wallet("frank", "EUR", new BigDecimal("200")));
        repo.save(new Wallet("frank", "GBP", new BigDecimal("150")));

        Page<Wallet> page = repo.findByOwnerIdOrderedByBalance(
            "frank", PageRequest.of(0, 2));

        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getContent().get(0).getBalance())
            .isGreaterThanOrEqualTo(page.getContent().get(1).getBalance());
    }

    @Test
    @Transactional
    void freezeAllActiveWalletsForOwner_freezesOnlyActiveOnes() {
        repo.save(new Wallet("grace", "USD", new BigDecimal("100")));
        repo.save(new Wallet("grace", "EUR", new BigDecimal("200")));
        Wallet alreadyFrozen = repo.save(new Wallet("grace", "GBP", new BigDecimal("300")));
        alreadyFrozen.freeze();
        repo.save(alreadyFrozen);
        repo.flush();

        int updated = repo.freezeAllActiveWalletsForOwner("grace");

        assertThat(updated).isEqualTo(2);

        repo.flush();
        List<Wallet> graceWallets = repo.findByOwnerId("grace");
        assertThat(graceWallets).allMatch(w -> w.getStatus() == WalletStatus.FROZEN);
    }

    @Test
    void sumBalanceByCurrency_sumsOnlyActiveWallets() {
        repo.save(new Wallet("u1", "USD", new BigDecimal("100")));
        repo.save(new Wallet("u2", "USD", new BigDecimal("200")));
        repo.save(new Wallet("u3", "EUR", new BigDecimal("50")));

        Wallet frozenUsd = repo.save(new Wallet("u4", "USD", new BigDecimal("500")));
        frozenUsd.freeze();
        repo.save(frozenUsd);

        List<Object[]> rows = repo.sumBalanceByCurrency();

        assertThat(rows).hasSize(2);
        rows.forEach(row -> {
            String currency = (String) row[0];
            BigDecimal sum = new BigDecimal(row[1].toString());
            if ("USD".equals(currency)) {
                assertThat(sum).isEqualByComparingTo("300");
            } else if ("EUR".equals(currency)) {
                assertThat(sum).isEqualByComparingTo("50");
            }
        });
    }

    @Test
    void walletCreditAndDebit_updateBalance() {
        Wallet wallet = repo.save(new Wallet("henry", "BTC", new BigDecimal("1.00000000")));

        wallet.credit(new BigDecimal("0.50000000"));
        repo.save(wallet);
        repo.flush();

        Wallet afterCredit = repo.findById(wallet.getId()).orElseThrow();
        assertThat(afterCredit.getBalance()).isEqualByComparingTo("1.50000000");

        afterCredit.debit(new BigDecimal("0.25000000"));
        repo.save(afterCredit);
        repo.flush();

        Wallet afterDebit = repo.findById(wallet.getId()).orElseThrow();
        assertThat(afterDebit.getBalance()).isEqualByComparingTo("1.25000000");
    }

    @Test
    void walletDebit_throwsOnInsufficientFunds() {
        Wallet wallet = repo.save(new Wallet("irene", "USD", new BigDecimal("100.00")));

        assertThatThrownBy(() -> wallet.debit(new BigDecimal("9999.00")))
            .isInstanceOf(Wallet.InsufficientFundsException.class);
    }
}
