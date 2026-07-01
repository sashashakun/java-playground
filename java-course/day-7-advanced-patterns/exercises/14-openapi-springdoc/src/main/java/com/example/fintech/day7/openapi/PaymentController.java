package com.example.fintech.day7.openapi;

import com.example.fintech.day7.openapi.PaymentDtos.CreatePaymentRequest;
import com.example.fintech.day7.openapi.PaymentDtos.PaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "Payment operations")
public class PaymentController {

    /** In-memory storage — good enough for documenting the API surface. */
    private final Map<String, PaymentResponse> store = new ConcurrentHashMap<>();

    @Operation(
            summary = "Create a payment",
            description = "Registers a new payment for the given merchant and returns it in PENDING status.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Payment created"),
            @ApiResponse(responseCode = "400", description = "Validation error — missing fields or non-positive amount")
    })
    @PostMapping
    public ResponseEntity<PaymentResponse> create(@RequestBody CreatePaymentRequest request) {
        if (request.amount() == null || request.amount().signum() <= 0
                || request.currency() == null || request.currency().isBlank()
                || request.merchantId() == null || request.merchantId().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        String id = "pay-" + UUID.randomUUID().toString().substring(0, 8);
        PaymentResponse payment = new PaymentResponse(
                id, request.amount(), request.currency(), request.merchantId(), "PENDING");
        store.put(id, payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @Operation(
            summary = "Get a payment by id",
            description = "Returns the payment with the given identifier, or 404 if it does not exist.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment found"),
            @ApiResponse(responseCode = "404", description = "No payment with the given id")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> get(@PathVariable String id) {
        PaymentResponse payment = store.get(id);
        return payment == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(payment);
    }
}
