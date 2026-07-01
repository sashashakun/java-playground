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
import java.time.Instant;
import java.util.List;

// SOLUTION G — Service: PaymentService

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

    @Transactional                                                         // TODO G1 ✓
    public Payment processPayment(String accountId, BigDecimal amount,
                                  String currency, String description) {
        var account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));

        account.debit(amount);
        accountRepository.save(account);

        Payment payment = new Payment(account, amount, currency, description);
        paymentRepository.save(payment);

        eventPublisher.publishEvent(                                        // TODO G1 ✓
            new DomainEvents.PaymentCreated(payment.getId(), accountId, amount));
        return payment;
    }

    @Transactional                                                         // TODO G2 ✓
    public Payment completePayment(String paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        payment.complete();
        paymentRepository.save(payment);
        eventPublisher.publishEvent(                                        // TODO G2 ✓
            new DomainEvents.PaymentCompleted(paymentId, Instant.now()));
        return payment;
    }

    @Transactional(readOnly = true)                                        // TODO G3 ✓
    public Page<Payment> findByAccount(String accountId, Pageable pageable) {
        return paymentRepository.findByAccount_Id(accountId, pageable);
    }

    @Transactional(readOnly = true)                                        // TODO G3 ✓
    public List<Payment> search(String accountId, PaymentStatus status, BigDecimal minAmount) {
        return paymentRepository.findAll(
            Specification.where(PaymentSpecs.forAccount(accountId))
                .and(PaymentSpecs.withStatus(status))
                .and(PaymentSpecs.amountAtLeast(minAmount))
        );
    }
}
