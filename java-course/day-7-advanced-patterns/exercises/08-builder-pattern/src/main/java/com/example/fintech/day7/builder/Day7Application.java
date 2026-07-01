package com.example.fintech.day7.builder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Day7Application {
    public static void main(String[] args) {
        SpringApplication.run(Day7Application.class, args);

        PaymentRequest request = new PaymentRequestBuilder()
                .amount(99.99)
                .currency("USD")
                .merchantId("merchant-001")
                .description("Demo payment")
                .idempotencyKey("key-abc-123")
                .addMetadata("region", "US")
                .build();

        System.out.println("Built: " + request);
    }
}
