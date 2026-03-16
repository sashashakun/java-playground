package com.example.fintech.day7.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Exercise 04 — Async Processing
 *
 * @Async makes a method run on a separate thread from the caller.
 * The method must return void or CompletableFuture<T>.
 * Spring wraps the invocation with the configured executor.
 *
 * TypeScript analogy: Promise.resolve().then(() => heavyWork()) — async without blocking.
 *
 * @EnableAsync (on Day7Application) activates the async AOP interceptor.
 * Same proxy rules as @Transactional: must be called from outside the bean.
 *
 * TODO 1: Annotate `generateReport` with @Async("reportExecutor").
 *         "reportExecutor" is the bean name defined in AsyncConfig.
 *         The method runs on the reportExecutor thread pool, not the caller's thread.
 *
 * TODO 2: Annotate `generateAllReports` with @Async("reportExecutor").
 *         This method fans out to generateReport for each request and combines results.
 *         Note: since generateAllReports itself is @Async, it must NOT call
 *         generateReport directly (self-invocation bypasses the proxy).
 *         Instead, call a separate bean or use CompletableFuture.supplyAsync.
 *         Simplification: just generate each report inline (no nested async call).
 */
@Service
public class ReportService {

    // TODO 1: @Async("reportExecutor")
    public CompletableFuture<Report> generateReport(ReportRequest request) {
        // Simulate CPU-intensive work (generating a report takes time)
        String reportId = "rpt-" + UUID.randomUUID().toString().substring(0, 8);
        String content = "Report for " + request.userId() + " over " + request.dayRange() + " days";
        String thread = Thread.currentThread().getName();

        Report report = new Report(reportId, request.userId(), request.reportType(),
            content, thread, Instant.now());

        return CompletableFuture.completedFuture(report);
    }

    // TODO 2: @Async("reportExecutor")
    public CompletableFuture<List<Report>> generateAllReports(List<ReportRequest> requests) {
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
