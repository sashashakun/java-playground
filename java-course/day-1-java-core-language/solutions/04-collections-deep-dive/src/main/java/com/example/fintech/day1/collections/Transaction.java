package com.example.fintech.day1.collections;

public record Transaction(String id, long amountCents, String currency, String type, String description) {}
