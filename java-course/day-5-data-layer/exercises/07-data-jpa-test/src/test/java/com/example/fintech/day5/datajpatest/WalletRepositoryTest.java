package com.example.fintech.day5.datajpatest;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Exercise 07 — DataJpaTest: Write the Tests
 *
 * The Wallet entity and WalletRepository are fully implemented.
 * Your job is to write @DataJpaTest tests that verify each repository method works.
 *
 * Key @DataJpaTest facts:
 *   ✓ Starts an in-memory H2 database
 *   ✓ Auto-configures Spring Data JPA repositories
 *   ✓ Each test runs in a transaction that is rolled back after the test
 *   ✓ Does NOT start the full Spring context (no web layer, no @Service beans)
 *   ✓ Inject repositories and TestEntityManager with @Autowired
 *
 * TODO 1: Test `findByOwnerId`
 *   - Save 2 wallets for owner "alice" and 1 for "bob"
 *   - Assert findByOwnerId("alice") returns exactly 2 results
 *   - Assert findByOwnerId("charlie") returns empty list
 *
 * TODO 2: Test `findByOwnerIdAndStatus`
 *   - Save 1 ACTIVE + 1 FROZEN wallet for owner "dave"
 *   - Assert findByOwnerIdAndStatus("dave", ACTIVE) returns 1 result
 *   - Assert findByOwnerIdAndStatus("dave", CLOSED) returns empty list
 *
 * TODO 3: Test `existsByOwnerIdAndCurrency`
 *   - Save a USD wallet for owner "eve"
 *   - Assert existsByOwnerIdAndCurrency("eve", "USD") is true
 *   - Assert existsByOwnerIdAndCurrency("eve", "EUR") is false
 *
 * TODO 4: Test `findHighBalanceActiveWallets`
 *   - Save 3 ACTIVE wallets with balances 100, 500, 1000
 *   - Save 1 FROZEN wallet with balance 2000 (should be excluded)
 *   - Assert findHighBalanceActiveWallets(BigDecimal("400")) returns 2 results (500, 1000)
 *
 * TODO 5: Test `findByOwnerIdOrderedByBalance` (pagination)
 *   - Save 3 wallets for "frank" with balances 50, 200, 150
 *   - Request page 0 with size 2
 *   - Assert page contains 2 items
 *   - Assert total pages = 2, total elements = 3
 *   - Assert first item has the highest balance (200)
 *
 * TODO 6: Test `freezeAllActiveWalletsForOwner` (@Modifying)
 *   - Save 2 ACTIVE + 1 FROZEN wallet for owner "grace"
 *   - Call freezeAllActiveWalletsForOwner("grace"), assert returns 2
 *   - Reload wallets, assert all are FROZEN
 *   NOTE: @Modifying queries require @Transactional on the test method
 *
 * TODO 7: Test `sumBalanceByCurrency` (native query)
 *   - Save: USD 100 + USD 200 + EUR 50 (all ACTIVE)
 *   - Save: USD 500 FROZEN (should be excluded)
 *   - Assert result contains 2 rows (USD and EUR)
 *   - Assert USD sum = 300, EUR sum = 50
 *
 * TODO 8: Test Wallet domain behaviour — credit and debit
 *   - Save a wallet with balance 1000 USD
 *   - Call wallet.credit(500), save, reload — assert balance is 1500
 *   - Call wallet.debit(200), save, reload — assert balance is 800 (after reload from 1000 + 500 - 200)
 *   - Assert wallet.debit(BigDecimal("9999")) throws InsufficientFundsException
 */
@DataJpaTest
class WalletRepositoryTest {

    @Autowired
    private WalletRepository repo;

    @Test
    void TODO1_findByOwnerId() {
        // TODO: implement this test
        throw new UnsupportedOperationException("TODO 1: not yet implemented");
    }

    @Test
    void TODO2_findByOwnerIdAndStatus() {
        // TODO: implement this test
        throw new UnsupportedOperationException("TODO 2: not yet implemented");
    }

    @Test
    void TODO3_existsByOwnerIdAndCurrency() {
        // TODO: implement this test
        throw new UnsupportedOperationException("TODO 3: not yet implemented");
    }

    @Test
    void TODO4_findHighBalanceActiveWallets() {
        // TODO: implement this test
        throw new UnsupportedOperationException("TODO 4: not yet implemented");
    }

    @Test
    void TODO5_paginatedFindByOwnerOrderedByBalance() {
        // TODO: implement this test
        throw new UnsupportedOperationException("TODO 5: not yet implemented");
    }

    @Test
    @Transactional
    void TODO6_freezeAllActiveWalletsForOwner() {
        // TODO: implement this test
        throw new UnsupportedOperationException("TODO 6: not yet implemented");
    }

    @Test
    void TODO7_sumBalanceByCurrency() {
        // TODO: implement this test
        throw new UnsupportedOperationException("TODO 7: not yet implemented");
    }

    @Test
    void TODO8_walletCreditAndDebit() {
        // TODO: implement this test
        throw new UnsupportedOperationException("TODO 8: not yet implemented");
    }
}
