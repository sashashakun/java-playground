package com.example.fintech.day7.decorator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Day7Application {
    public static void main(String[] args) {
        SpringApplication.run(Day7Application.class, args);

        PaymentService service = new AuditingPaymentService(new SimplePaymentService());
        String result = service.processPayment("merchant-001", 150.00, "USD");
        System.out.println("Final result: " + result);
    }
}
