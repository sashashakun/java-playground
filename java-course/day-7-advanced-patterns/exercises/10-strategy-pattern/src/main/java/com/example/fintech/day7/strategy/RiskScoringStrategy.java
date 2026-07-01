package com.example.fintech.day7.strategy;

@FunctionalInterface
public interface RiskScoringStrategy {
    RiskLevel score(double amount, String currency);
}
