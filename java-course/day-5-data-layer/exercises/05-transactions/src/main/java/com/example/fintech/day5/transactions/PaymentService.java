package com.example.fintech.day5.transactions;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Exercise 05 — Transactions
 *
 * Spring's @Transactional is declarative: annotate a method and Spring wraps every
 * call in a database transaction, committing on success and rolling back on any
 * unchecked exception (RuntimeException / Error).
 *
 * TypeScript analogy: Knex / Prisma's $transaction(() => { ... }) — but declarative,
 * not manual. No try/catch or commit/rollback calls needed.
 *
 * TODO 1: Annotate `createPayment` with @Transactional.
 *         This method saves a new payment and must roll back completely if the
 *         idempotency key already exists (a DuplicateKeyException is thrown).
 *
 * TODO 2: Annotate `findAll` with @Transactional(readOnly = true).
 *         readOnly = true: Hibernate skips dirty-checking on load → faster reads.
 *         TypeScript analogy: Prisma's $transaction with { readOnly: true }.
 *
 * TODO 3: Annotate `completePayment` with @Transactional.
 *         If the payment is not found, throw PaymentNotFoundException.
 *         The thrown exception causes the transaction to roll back.
 *
 * TODO 4: Annotate `batchApprove` with @Transactional.
 *         This method approves a list of payment IDs.
 *         If ANY payment is not found, the whole batch must roll back.
 *         (No extra code needed — @Transactional handles this automatically.)
 *
 * TODO 5: Annotate `findById` with @Transactional(readOnly = true).
 */
@Service
public class PaymentService {

    private final PaymentRepository repo;

    public PaymentService(PaymentRepository repo) {
        this.repo = repo;
    }

    // TODO 1: @Transactional
    public Payment createPayment(BigDecimal amount, String currency,
                                 String merchantId, String description,
                                 String idempotencyKey) {
        if (repo.existsByIdempotencyKey(idempotencyKey)) {
            throw new DuplicateKeyException("Idempotency key already used: " + idempotencyKey);
        }
        Money money = new Money(amount, currency);
        return repo.save(new Payment(money, merchantId, description, idempotencyKey));
    }

    // TODO 2: @Transactional(readOnly = true)
    public List<Payment> findAll() {
        return repo.findAll();
    }

    // TODO 3: @Transactional
    public Payment completePayment(String paymentId) {
        Payment payment = repo.findById(paymentId)
            .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        payment.setStatus(PaymentStatus.COMPLETED);
        return repo.save(payment);
    }

    // TODO 4: @Transactional
    public List<Payment> batchApprove(List<String> paymentIds) {
        return paymentIds.stream()
            .map(id -> repo.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException(id)))
            .peek(p -> p.setStatus(PaymentStatus.PROCESSING))
            .map(repo::save)
            .toList();
    }

    // TODO 5: @Transactional(readOnly = true)
    public Optional<Payment> findById(String id) {
        return repo.findById(id);
    }

    // --- Domain exceptions (already implemented) ---

    public static class PaymentNotFoundException extends RuntimeException {
        public PaymentNotFoundException(String id) {
            super("Payment not found: " + id);
        }
    }

    public static class DuplicateKeyException extends RuntimeException {
        public DuplicateKeyException(String message) {
            super(message);
        }
    }
}
