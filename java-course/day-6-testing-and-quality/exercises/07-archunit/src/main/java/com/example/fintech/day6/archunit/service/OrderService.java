package com.example.fintech.day6.archunit.service;

import com.example.fintech.day6.archunit.model.Order;
import com.example.fintech.day6.archunit.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repo;

    public OrderService(OrderRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Order placeOrder(String customerId, BigDecimal amount) {
        return repo.save(new Order(customerId, amount));
    }

    @Transactional
    public Order confirmOrder(String orderId) {
        Order order = repo.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.confirm();
        return repo.save(order);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersForCustomer(String customerId) {
        return repo.findByCustomerId(customerId);
    }

    public static class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException(String id) {
            super("Order not found: " + id);
        }
    }
}
