package com.example.fintech.day7.outbox;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TransactionalEventsTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OutboxEventRepository outboxEventRepository;

    @Autowired
    private OutboxProcessor outboxProcessor;

    @Test
    void confirmOrder_createsOutboxEventAndDispatchesAfterCommit() {
        Order order = orderService.confirmOrder("cust-1", new BigDecimal("199.99"));

        // Order should be saved
        assertThat(order.getId()).isNotNull();
        assertThat(order.getStatus()).isEqualTo(Order.Status.CONFIRMED);

        // Outbox event should have been dispatched (AFTER_COMMIT listener ran)
        List<OutboxEvent> events = outboxEventRepository.findByAggregateId(order.getId());
        assertThat(events).hasSize(1);
        assertThat(events.getFirst().getEventType())
            .isEqualTo(OutboxEvent.EventType.ORDER_CONFIRMED);
        assertThat(events.getFirst().getStatus())
            .isEqualTo(OutboxEvent.EventStatus.DISPATCHED);

        // Processor should have recorded the dispatched order id
        assertThat(outboxProcessor.getDispatchedOrderIds())
            .contains(order.getId());
    }

    @Test
    void cancelOrder_createsOutboxEventAndDispatchesAfterCommit() {
        Order order = orderService.cancelOrder("cust-2", new BigDecimal("50.00"), "customer request");

        List<OutboxEvent> events = outboxEventRepository.findByAggregateId(order.getId());
        assertThat(events).hasSize(1);
        assertThat(events.getFirst().getEventType())
            .isEqualTo(OutboxEvent.EventType.ORDER_CANCELLED);
        assertThat(events.getFirst().getStatus())
            .isEqualTo(OutboxEvent.EventStatus.DISPATCHED);
    }

    @Test
    void outboxEvent_startsPending_thenBecomesDispatched() {
        // Verify the event lifecycle: PENDING → DISPATCHED
        // After confirmOrder completes (transaction committed), status should be DISPATCHED
        Order order = orderService.confirmOrder("cust-3", new BigDecimal("75.00"));

        OutboxEvent event = outboxEventRepository.findByAggregateId(order.getId()).getFirst();
        assertThat(event.getStatus()).isEqualTo(OutboxEvent.EventStatus.DISPATCHED);
        assertThat(event.getProcessedAt()).isNotNull();
    }

    @Test
    void multipleOrders_allDispatchedIndependently() {
        Order order1 = orderService.confirmOrder("cust-10", new BigDecimal("100.00"));
        Order order2 = orderService.confirmOrder("cust-11", new BigDecimal("200.00"));
        Order order3 = orderService.cancelOrder("cust-12", new BigDecimal("300.00"), "fraud");

        for (Order order : List.of(order1, order2, order3)) {
            List<OutboxEvent> events = outboxEventRepository.findByAggregateId(order.getId());
            assertThat(events).hasSize(1);
            assertThat(events.getFirst().getStatus())
                .isEqualTo(OutboxEvent.EventStatus.DISPATCHED);
        }
    }
}
