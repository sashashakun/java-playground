package com.example.fintech.day7.validation;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @PostMapping
    public ResponseEntity<Map<String, String>> createPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(Map.of(
            "status", "accepted",
            "userId", request.userId(),
            "currency", request.currency()
        ));
    }
}
