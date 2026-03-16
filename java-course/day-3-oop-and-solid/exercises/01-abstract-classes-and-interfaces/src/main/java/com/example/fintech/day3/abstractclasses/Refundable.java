package com.example.fintech.day3.abstractclasses;

import java.math.BigDecimal;

/**
 * Exercise 01 — Part B: Interface Segregation preview
 *
 * A gateway that supports refunds ALSO implements this interface.
 * Not all gateways must support refunds — that's the point of a separate interface.
 */
public interface Refundable {

    /**
     * Issue a full or partial refund for an existing transaction.
     *
     * @param transactionId  the ID returned by the original charge
     * @param amount         the amount to refund (must be > 0 and ≤ original charge)
     * @return RefundResult indicating success/failure
     */
    PaymentGateway.RefundResult refund(String transactionId, BigDecimal amount);
}
