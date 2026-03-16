package com.example.fintech.capstone.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

// SOLUTION I — Service: ReportService

@Service
public class ReportService {

    @Async("reportExecutor")                            // TODO I1 ✓
    public CompletableFuture<Report> generateReport(String ownerId, String reportType) {
        String reportId = "rpt-" + UUID.randomUUID().toString().substring(0, 8);
        String thread = Thread.currentThread().getName();
        return CompletableFuture.completedFuture(
            new Report(reportId, ownerId, reportType, thread, Instant.now()));
    }

    public record Report(String id, String ownerId, String type,
                         String generatedByThread, Instant generatedAt) {}
}
