package com.example.fintech.day4.profiles;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Exercise 07 — Simple controller to show which gateway is active.
 * Provided — no changes needed.
 */
@RestController
@RequestMapping("/api/gateway")
public class GatewayInfoController {

    private final PaymentGateway gateway;

    public GatewayInfoController(PaymentGateway gateway) {
        this.gateway = gateway;
    }

    @GetMapping("/info")
    public Map<String, String> info() {
        return Map.of("activeGateway", gateway.getGatewayName());
    }
}
