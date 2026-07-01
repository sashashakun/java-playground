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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

// SOLUTION K — Web: PaymentController

@RestController                                         // TODO K1 ✓
@RequestMapping("/payments")                           // TODO K1 ✓
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping                                        // TODO K2 ✓
    public ResponseEntity<PaymentResponse> processPayment(
            @RequestBody @Valid CreatePaymentRequest request,   // TODO K2 ✓
            UriComponentsBuilder uriBuilder) {
        var payment = paymentService.processPayment(
            request.accountId(), request.amount(), request.currency(), request.description());
        var location = uriBuilder.path("/payments/{id}").buildAndExpand(payment.getId()).toUri();
        return ResponseEntity.created(location).body(PaymentResponse.from(payment));
    }

    @GetMapping                                         // TODO K3 ✓
    public Page<PaymentResponse> getByAccount(
            @RequestParam("accountId") String accountId, // TODO K3 ✓
            Pageable pageable) {
        return paymentService.findByAccount(accountId, pageable)
            .map(PaymentResponse::from);
    }

    @GetMapping("/search")                              // TODO K3 ✓
    public List<PaymentResponse> search(
            @RequestParam(required = false) String accountId,      // TODO K3 ✓
            @RequestParam(required = false) PaymentStatus status,  // TODO K3 ✓
            @RequestParam(required = false) BigDecimal minAmount) { // TODO K3 ✓
        return paymentService.search(accountId, status, minAmount).stream()
            .map(PaymentResponse::from)
            .toList();
    }
}
