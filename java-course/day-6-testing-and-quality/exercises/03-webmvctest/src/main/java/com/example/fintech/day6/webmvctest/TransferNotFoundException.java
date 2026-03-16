package com.example.fintech.day6.webmvctest;

public class TransferNotFoundException extends RuntimeException {
    public TransferNotFoundException(String id) {
        super("Transfer not found: " + id);
    }
}
