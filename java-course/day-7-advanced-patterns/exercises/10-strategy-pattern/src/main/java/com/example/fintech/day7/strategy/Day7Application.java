package com.example.fintech.day7.strategy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Day7Application {
    public static void main(String[] args) {
        SpringApplication.run(Day7Application.class, args);

        // High-value strategy: > $1000 is HIGH, > $100 is MEDIUM, else LOW
        RiskScoringStrategy highValueStrategy =
                (amount, currency) -> amount > 1000 ? RiskLevel.HIGH
                        : amount > 100 ? RiskLevel.MEDIUM
                        : RiskLevel.LOW;

        // Crypto strategy: BTC is always HIGH
        RiskScoringStrategy cryptoStrategy =
                (amount, currency) -> "BTC".equals(currency) ? RiskLevel.HIGH
                        : highValueStrategy.score(amount, currency);

        RiskEngine engine = new RiskEngine(highValueStrategy);
        System.out.println("$50 USD: " + engine.assessRisk(50, "USD"));
        System.out.println("$500 USD: " + engine.assessRisk(500, "USD"));
        System.out.println("$5000 USD: " + engine.assessRisk(5000, "USD"));

        engine.setStrategy(cryptoStrategy);
        System.out.println("$10 BTC (crypto): " + engine.assessRisk(10, "BTC"));
    }
}
