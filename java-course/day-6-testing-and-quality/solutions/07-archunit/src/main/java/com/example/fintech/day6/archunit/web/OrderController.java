package com.example.fintech.day6.archunit.web;

import com.example.fintech.day6.archunit.model.Order;
import com.example.fintech.day6.archunit.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    public record PlaceOrderRequest(
        @NotBlank String customerId,
        @NotNull @DecimalMin("0.01") BigDecimal amount
    ) {}

    @PostMapping
    public ResponseEntity<Order> place(@Valid @RequestBody PlaceOrderRequest req) {
        Order order = service.placeOrder(req.customerId(), req.amount());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}").buildAndExpand(order.getId()).toUri();
        return ResponseEntity.created(location).body(order);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Order> confirm(@PathVariable String id) {
        return ResponseEntity.ok(service.confirmOrder(id));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getByCustomer(@RequestParam String customerId) {
        return ResponseEntity.ok(service.getOrdersForCustomer(customerId));
    }
}
