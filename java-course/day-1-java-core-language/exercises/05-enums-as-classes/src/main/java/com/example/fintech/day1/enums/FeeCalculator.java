package com.example.fintech.day1.enums;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Exercise 05 — Enums as Classes (Part 3 of 3)
 *
 * A fee calculator that uses TransactionType and Currency enums
 * to compute fees for transactions.
 *
 * Fee schedule:
 *   - FEE type transactions: no additional fee (they ARE the fee)
 *   - Crypto currencies (BTC, ETH): 0.5% of amount, min 50 cents
 *   - PURCHASE type: 1.5% of amount
 *   - TRANSFER_OUT type: flat $0.25 (25 cents)
 *   - All other types: no fee (0)
 *
 * Implement the 2 TODO methods.
 */
public class FeeCalculator {

    // Fee constants — use BigDecimal for exact arithmetic
    private static final BigDecimal CRYPTO_RATE    = new BigDecimal("0.005");  // 0.5%
    private static final BigDecimal PURCHASE_RATE  = new BigDecimal("0.015");  // 1.5%
    private static final long CRYPTO_MIN_CENTS      = 50L;    // $0.50 minimum
    private static final long TRANSFER_FEE_CENTS    = 25L;    // $0.25 flat

    /**
     * TODO 1 — Calculate the fee in cents for a given transaction.
     *
     * Use a switch expression on transactionType, then check the currency.
     *
     * Fee rules (in order of priority):
     *   1. If transactionType == FEE → return 0 (fees don't incur fees)
     *   2. If currency.isCrypto() → fee = max(amountCents * 0.5%, CRYPTO_MIN_CENTS)
     *      Calculation: BigDecimal fee = BigDecimal.valueOf(amountCents)
     *                       .multiply(CRYPTO_RATE)
     *                       .setScale(0, RoundingMode.HALF_EVEN)
     *                   return Math.max(fee.longValue(), CRYPTO_MIN_CENTS)
     *   3. If transactionType == PURCHASE → fee = amountCents * 1.5%, rounded HALF_EVEN
     *   4. If transactionType == TRANSFER_OUT → return TRANSFER_FEE_CENTS (25)
     *   5. Otherwise → return 0
     *
     * Examples:
     *   calculateFee(10000L, TransactionType.PURCHASE, Currency.USD)   → 150L  (1.5% of $100)
     *   calculateFee(10000L, TransactionType.DEPOSIT,  Currency.BTC)   → 50L   (0.5% of $100 = $0.50, same as min)
     *   calculateFee(100000L,TransactionType.DEPOSIT,  Currency.BTC)   → 500L  (0.5% of $1000 = $5.00 > min)
     *   calculateFee(5000L,  TransactionType.TRANSFER_OUT, Currency.EUR) → 25L (flat fee)
     *   calculateFee(5000L,  TransactionType.DEPOSIT, Currency.USD)    → 0L   (no fee)
     *   calculateFee(5000L,  TransactionType.FEE, Currency.USD)        → 0L   (never fee a fee)
     */
    public long calculateFee(long amountCents, TransactionType transactionType, Currency currency) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * TODO 2 — Check if a fee applies to this transaction.
     *
     * Returns true if calculateFee(...) would return > 0.
     * Do NOT call calculateFee() internally — reuse the same logic (switch expression).
     *
     * Alternatively: return calculateFee(amountCents, type, currency) > 0;
     * That's fine — feel free to call calculateFee here if you prefer.
     */
    public boolean hasFee(long amountCents, TransactionType transactionType, Currency currency) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
