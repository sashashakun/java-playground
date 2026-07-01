package com.example.fintech.day7.observer;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComplianceEventBusTest {

    @Test
    void subscriberReceivesPublishedTransactionCreatedEvent() {
        ComplianceEventBus bus = new ComplianceEventBus();
        List<ComplianceEvent> received = new ArrayList<>();

        bus.subscribe(received::add);
        bus.publish(new ComplianceEvent.TransactionCreated("tx-001", 100.00));

        assertEquals(1, received.size());
        assertInstanceOf(ComplianceEvent.TransactionCreated.class, received.get(0));
        ComplianceEvent.TransactionCreated tc = (ComplianceEvent.TransactionCreated) received.get(0);
        assertEquals("tx-001", tc.txId());
        assertEquals(100.00, tc.amount());
    }

    @Test
    void subscriberReceivesPublishedTransactionFlaggedEvent() {
        ComplianceEventBus bus = new ComplianceEventBus();
        List<ComplianceEvent> received = new ArrayList<>();

        bus.subscribe(received::add);
        bus.publish(new ComplianceEvent.TransactionFlagged("tx-002", "Suspicious activity"));

        assertEquals(1, received.size());
        assertInstanceOf(ComplianceEvent.TransactionFlagged.class, received.get(0));
        ComplianceEvent.TransactionFlagged tf = (ComplianceEvent.TransactionFlagged) received.get(0);
        assertEquals("tx-002", tf.txId());
        assertEquals("Suspicious activity", tf.reason());
    }

    @Test
    void multipleSubscribersAllReceiveEvent() {
        ComplianceEventBus bus = new ComplianceEventBus();
        List<String> log1 = new ArrayList<>();
        List<String> log2 = new ArrayList<>();
        List<String> log3 = new ArrayList<>();

        bus.subscribe(e -> log1.add("subscriber1"));
        bus.subscribe(e -> log2.add("subscriber2"));
        bus.subscribe(e -> log3.add("subscriber3"));

        bus.publish(new ComplianceEvent.TransactionCreated("tx-003", 200.00));

        assertEquals(1, log1.size());
        assertEquals(1, log2.size());
        assertEquals(1, log3.size());
    }

    @Test
    void noSubscribersPublishDoesNotThrow() {
        ComplianceEventBus bus = new ComplianceEventBus();
        assertDoesNotThrow(() ->
                bus.publish(new ComplianceEvent.TransactionCreated("tx-004", 50.00)));
    }

    @Test
    void publishMultipleEventsAllDelivered() {
        ComplianceEventBus bus = new ComplianceEventBus();
        List<ComplianceEvent> received = new ArrayList<>();
        bus.subscribe(received::add);

        bus.publish(new ComplianceEvent.TransactionCreated("tx-005", 300.00));
        bus.publish(new ComplianceEvent.TransactionFlagged("tx-005", "Double spend attempt"));
        bus.publish(new ComplianceEvent.TransactionCreated("tx-006", 50.00));

        assertEquals(3, received.size());
    }

    @Test
    void switchExpressionDispatchWorksOnReceivedEvents() {
        ComplianceEventBus bus = new ComplianceEventBus();
        List<String> dispatched = new ArrayList<>();

        bus.subscribe(event -> {
            String label = switch (event) {
                case ComplianceEvent.TransactionCreated tc -> "CREATED:" + tc.txId();
                case ComplianceEvent.TransactionFlagged tf -> "FLAGGED:" + tf.txId();
            };
            dispatched.add(label);
        });

        bus.publish(new ComplianceEvent.TransactionCreated("tx-007", 999.00));
        bus.publish(new ComplianceEvent.TransactionFlagged("tx-008", "AML check failed"));

        assertEquals("CREATED:tx-007", dispatched.get(0));
        assertEquals("FLAGGED:tx-008", dispatched.get(1));
    }
}
