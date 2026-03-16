package com.example.fintech.day5.datajpatest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Exercise 07 — DataJpaTest
 *
 * Full implementation provided. Write tests in WalletRepositoryTest.java.
 */
public interface WalletRepository extends JpaRepository<Wallet, String> {

    List<Wallet> findByOwnerId(String ownerId);

    List<Wallet> findByOwnerIdAndStatus(String ownerId, WalletStatus status);

    Optional<Wallet> findByOwnerIdAndCurrency(String ownerId, String currency);

    boolean existsByOwnerIdAndCurrency(String ownerId, String currency);

    @Query("SELECT w FROM Wallet w WHERE w.balance > :threshold AND w.status = 'ACTIVE'")
    List<Wallet> findHighBalanceActiveWallets(@Param("threshold") BigDecimal threshold);

    @Query("SELECT w FROM Wallet w WHERE w.ownerId = :ownerId ORDER BY w.balance DESC")
    Page<Wallet> findByOwnerIdOrderedByBalance(
        @Param("ownerId") String ownerId, Pageable pageable);

    @Modifying
    @Query("UPDATE Wallet w SET w.status = 'FROZEN' WHERE w.ownerId = :ownerId AND w.status = 'ACTIVE'")
    int freezeAllActiveWalletsForOwner(@Param("ownerId") String ownerId);

    @Query(value = "SELECT currency, SUM(balance) FROM wallets WHERE status = 'ACTIVE' GROUP BY currency",
           nativeQuery = true)
    List<Object[]> sumBalanceByCurrency();
}
