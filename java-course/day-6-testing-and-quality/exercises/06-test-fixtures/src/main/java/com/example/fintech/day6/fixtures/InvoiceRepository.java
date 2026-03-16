package com.example.fintech.day6.fixtures;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    List<Invoice> findByMerchantId(String merchantId);
    List<Invoice> findByStatus(Invoice.InvoiceStatus status);
}
