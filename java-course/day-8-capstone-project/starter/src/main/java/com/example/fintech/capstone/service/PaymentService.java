package com.example.fintech.capstone.service;

import com.example.fintech.capstone.domain.DomainEvents;
import com.example.fintech.capstone.domain.Payment;
import com.example.fintech.capstone.domain.PaymentStatus;
import com.example.fintech.capstone.exception.AccountNotFoundException;
import com.example.fintech.capstone.exception.PaymentNotFoundException;
import com.example.fintech.capstone.repository.AccountRepository;
import com.example.fintech.capstone.repository.PaymentRepository;
import com.example.fintech.capstone.repository.PaymentSpecs;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Capstone Exercise G — Service: PaymentService
 *
 * TODO G1: Annotate processPayment with @Transactional.
 *          The method debits the account and saves the payment in ONE transaction.
 *          After save, publish DomainEvents.PaymentCreated.
 *
 * TODO G2: Annotate completePayment with @Transactional.
 *          Mark payment COMPLETED, publish DomainEvents.PaymentCompleted.
 *
 * TODO G3: Annotate findByAccount with @Transactional(readOnly = true).
 *          Annotate search with @Transactional(readOnly = true).
 */
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PaymentService(PaymentRepository paymentRepository,
                          AccountRepository accountRepository,
                          ApplicationEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.accountRepository = accountRepository;
        this.eventPublisher = eventPublisher;
    }

    // TODO G1: @Transactional
    public Payment processPayment(String accountId, BigDecimal amount,
                                  String currency, String description) {
        var account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));

        account.debit(amount);
        accountRepository.save(account);

        Payment payment = new Payment(account, amount, currency, description);
        paymentRepository.save(payment);

        // TODO G1: eventPublisher.publishEvent(new DomainEvents.PaymentCreated(...))
        return payment;
    }

    // TODO G2: @Transactional
    public Payment completePayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        payment.complete();
        paymentRepository.save(payment);
        // TODO G2: eventPublisher.publishEvent(new DomainEvents.PaymentCompleted(...))
        return payment;
    }

    // TODO G3: @Transactional(readOnly = true)
    public Page<Payment> findByAccount(String accountId, Pageable pageable) {
        return paymentRepository.findByAccount_Id(accountId, pageable);
    }

    // TODO G3: @Transactional(readOnly = true)
    public List<Payment> search(String accountId, PaymentStatus status, BigDecimal minAmount) {
        return paymentRepository.findAll(
            Specification.where(PaymentSpecs.forAccount(accountId))
                .and(PaymentSpecs.withStatus(status))
                .and(PaymentSpecs.amountAtLeast(minAmount))
        );
    }
}
