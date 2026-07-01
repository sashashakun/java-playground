package com.example.fintech.day7.observer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Day7Application {
    public static void main(String[] args) {
        SpringApplication.run(Day7Application.class, args);

        ComplianceEventBus bus = new ComplianceEventBus();

        bus.subscribe(event -> {
            switch (event) {
                case ComplianceEvent.TransactionCreated tc ->
                        System.out.println("[LOG] Transaction created: " + tc.txId() + " amount=" + tc.amount());
                case ComplianceEvent.TransactionFlagged tf ->
                        System.out.println("[ALERT] Transaction flagged: " + tf.txId() + " reason=" + tf.reason());
            }
        });

        bus.publish(new ComplianceEvent.TransactionCreated("tx-001", 500.00));
        bus.publish(new ComplianceEvent.TransactionFlagged("tx-002", "Amount exceeds daily limit"));
    }
}
