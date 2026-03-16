package com.example.fintech.day7.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

// SOLUTION 04 — Async: ReportService

@Service
public class ReportService {

    @Async("reportExecutor")                                // TODO 1 ✓
    public CompletableFuture<Report> generateReport(ReportRequest request) {
        String reportId = "rpt-" + UUID.randomUUID().toString().substring(0, 8);
        String content = "Report for " + request.userId() + " over " + request.dayRange() + " days";
        String thread = Thread.currentThread().getName();

        Report report = new Report(reportId, request.userId(), request.reportType(),
            content, thread, Instant.now());

        return CompletableFuture.completedFuture(report);
    }

    @Async("reportExecutor")                                // TODO 2 ✓
    public CompletableFuture<List<Report>> generateAllReports(List<ReportRequest> requests) {
        // NOTE: Do NOT call this.generateReport() here — self-invocation bypasses proxy.
        // Generate inline instead.
        List<Report> reports = requests.stream()
            .map(req -> {
                String reportId = "rpt-" + UUID.randomUUID().toString().substring(0, 8);
                return new Report(reportId, req.userId(), req.reportType(),
                    "Batch report", Thread.currentThread().getName(), Instant.now());
            })
            .toList();

        return CompletableFuture.completedFuture(reports);
    }
}
