package com.example.fintech.capstone.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Capstone Exercise I — Service: ReportService
 *
 * Report generation is CPU/IO-intensive. Running it on the HTTP thread blocks
 * other requests. @Async delegates to the "reportExecutor" thread pool.
 *
 * TODO I1: Add @Async("reportExecutor") to generateReport.
 *          The test verifies that Thread.currentThread().getName() contains "report-".
 */
@Service
public class ReportService {

    // TODO I1: @Async("reportExecutor")
    public CompletableFuture<Report> generateReport(String ownerId, String reportType) {
        String reportId = "rpt-" + UUID.randomUUID().toString().substring(0, 8);
        String thread = Thread.currentThread().getName();
        return CompletableFuture.completedFuture(
            new Report(reportId, ownerId, reportType, thread, Instant.now()));
    }

    public record Report(String id, String ownerId, String type,
                         String generatedByThread, Instant generatedAt) {}
}
