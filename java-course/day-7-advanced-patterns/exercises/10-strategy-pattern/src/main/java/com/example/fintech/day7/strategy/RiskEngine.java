package com.example.fintech.day7.strategy;

public class RiskEngine {
    private RiskScoringStrategy strategy;

    public RiskEngine(RiskScoringStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(RiskScoringStrategy strategy) {
        this.strategy = strategy;
    }

    public RiskLevel assessRisk(double amount, String currency) {
        return strategy.score(amount, currency);
    }
}
