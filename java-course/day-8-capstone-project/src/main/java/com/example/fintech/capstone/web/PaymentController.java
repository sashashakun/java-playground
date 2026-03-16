package com.example.fintech.capstone.web;

import com.example.fintech.capstone.domain.PaymentStatus;
import com.example.fintech.capstone.service.PaymentService;
import com.example.fintech.capstone.web.dto.CreatePaymentRequest;
import com.example.fintech.capstone.web.dto.PaymentResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Capstone Exercise K — Web: PaymentController
 *
 * TODO K1: Add @RestController and @RequestMapping("/payments") to this class.
 *
 * TODO K2: Annotate processPayment:
 *          - @PostMapping → 201 Created with Location /payments/{id}
 *          - @RequestBody @Valid on the request parameter
 *
 * TODO K3: Annotate getByAccount:
 *          - @GetMapping with @RequestParam("accountId") and Pageable parameter
 *          - Returns Page<PaymentResponse>
 *
 *         Annotate search:
 *          - @GetMapping("/search") with optional @RequestParam params
 *          - Returns List<PaymentResponse>
 */
// TODO K1: @RestController @RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // TODO K2: @PostMapping — 201 Created
    public ResponseEntity<PaymentResponse> processPayment(
            // TODO K2: @RequestBody @Valid
            CreatePaymentRequest request,
            UriComponentsBuilder uriBuilder) {
        var payment = paymentService.processPayment(
            request.accountId(), request.amount(), request.currency(), request.description());
        var location = uriBuilder.path("/payments/{id}").buildAndExpand(payment.getId()).toUri();
        return ResponseEntity.created(location).body(PaymentResponse.from(payment));
    }

    // TODO K3: @GetMapping — paginated list
    public Page<PaymentResponse> getByAccount(
            // TODO K3: @RequestParam("accountId")
            String accountId,
            Pageable pageable) {
        return paymentService.findByAccount(accountId, pageable)
            .map(PaymentResponse::from);
    }

    // TODO K3: @GetMapping("/search") — filtered list
    public List<PaymentResponse> search(
            // TODO K3: @RequestParam(required = false) on each
            String accountId,
            PaymentStatus status,
            BigDecimal minAmount) {
        return paymentService.search(accountId, status, minAmount).stream()
            .map(PaymentResponse::from)
            .toList();
    }
}
