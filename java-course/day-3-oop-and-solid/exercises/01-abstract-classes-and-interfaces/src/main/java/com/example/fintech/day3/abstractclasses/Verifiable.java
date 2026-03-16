package com.example.fintech.day3.abstractclasses;

/**
 * Exercise 01 — Part B: Verifiable interface
 *
 * A gateway that can verify transaction status implements this interface.
 */
public interface Verifiable {

    /**
     * Verify that a transaction completed successfully on the gateway's side.
     *
     * @param transactionId the transaction to verify
     * @return true if the transaction is confirmed settled
     */
    boolean verify(String transactionId);
}
