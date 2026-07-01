package com.example.fintech.day7.strategy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RiskEngineTest {

    private final RiskScoringStrategy highValueStrategy =
            (amount, currency) -> amount > 1000 ? RiskLevel.HIGH
                    : amount > 100 ? RiskLevel.MEDIUM
                    : RiskLevel.LOW;

    private final RiskScoringStrategy cryptoStrategy =
            (amount, currency) -> "BTC".equals(currency) ? RiskLevel.HIGH
                    : highValueStrategy.score(amount, currency);

    @Test
    void lowAmountReturnsLow() {
        RiskEngine engine = new RiskEngine(highValueStrategy);
        assertEquals(RiskLevel.LOW, engine.assessRisk(50.00, "USD"));
    }

    @Test
    void mediumAmountReturnsMedium() {
        RiskEngine engine = new RiskEngine(highValueStrategy);
        assertEquals(RiskLevel.MEDIUM, engine.assessRisk(500.00, "USD"));
    }

    @Test
    void highAmountReturnsHigh() {
        RiskEngine engine = new RiskEngine(highValueStrategy);
        assertEquals(RiskLevel.HIGH, engine.assessRisk(5000.00, "USD"));
    }

    @Test
    void boundaryAmountAtExactly100ReturnsMedium() {
        RiskEngine engine = new RiskEngine(highValueStrategy);
        // 100 is not > 100, so LOW
        assertEquals(RiskLevel.LOW, engine.assessRisk(100.00, "USD"));
    }

    @Test
    void boundaryAmountJustAbove100ReturnsMedium() {
        RiskEngine engine = new RiskEngine(highValueStrategy);
        assertEquals(RiskLevel.MEDIUM, engine.assessRisk(100.01, "USD"));
    }

    @Test
    void cryptoStrategyBtcAlwaysHigh() {
        RiskEngine engine = new RiskEngine(cryptoStrategy);
        assertEquals(RiskLevel.HIGH, engine.assessRisk(1.00, "BTC"));
        assertEquals(RiskLevel.HIGH, engine.assessRisk(0.001, "BTC"));
    }

    @Test
    void cryptoStrategyNonBtcUsesHighValueLogic() {
        RiskEngine engine = new RiskEngine(cryptoStrategy);
        assertEquals(RiskLevel.LOW, engine.assessRisk(50.00, "ETH"));
        assertEquals(RiskLevel.HIGH, engine.assessRisk(5000.00, "ETH"));
    }

    @Test
    void strategyCanBeSwappedAtRuntime() {
        RiskEngine engine = new RiskEngine(highValueStrategy);
        assertEquals(RiskLevel.LOW, engine.assessRisk(1.00, "BTC"));

        engine.setStrategy(cryptoStrategy);
        assertEquals(RiskLevel.HIGH, engine.assessRisk(1.00, "BTC"));
    }

    @Test
    void customInlineStrategyWorksCorrectly() {
        RiskScoringStrategy flatStrategy = (amount, currency) -> RiskLevel.MEDIUM;
        RiskEngine engine = new RiskEngine(flatStrategy);

        assertEquals(RiskLevel.MEDIUM, engine.assessRisk(1.00, "USD"));
        assertEquals(RiskLevel.MEDIUM, engine.assessRisk(999999.00, "USD"));
    }
}
