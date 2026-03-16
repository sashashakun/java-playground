package com.example.fintech.day7.async;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AsyncTest {

    @Autowired
    private ReportService reportService;

    @Test
    void generateReport_runsOnDifferentThread() throws Exception {
        String callerThread = Thread.currentThread().getName();

        CompletableFuture<Report> future = reportService.generateReport(
            new ReportRequest("user-1", "MONTHLY", 30));

        Report report = future.get(5, TimeUnit.SECONDS);

        assertThat(report).isNotNull();
        assertThat(report.reportId()).startsWith("rpt-");
        assertThat(report.userId()).isEqualTo("user-1");

        // The report should have been generated on a DIFFERENT thread (the executor pool)
        assertThat(report.generatedByThread())
            .isNotEqualTo(callerThread)
            .contains("report-async-"); // matches our threadNamePrefix
    }

    @Test
    void generateAllReports_runsConcurrently() throws Exception {
        List<ReportRequest> requests = List.of(
            new ReportRequest("user-1", "MONTHLY", 30),
            new ReportRequest("user-2", "WEEKLY", 7),
            new ReportRequest("user-3", "DAILY", 1)
        );

        CompletableFuture<List<Report>> future = reportService.generateAllReports(requests);
        List<Report> reports = future.get(5, TimeUnit.SECONDS);

        assertThat(reports).hasSize(3);
        assertThat(reports).extracting("userId")
            .containsExactlyInAnyOrder("user-1", "user-2", "user-3");
    }

    @Test
    void generateReport_returnsCompletableFuture() {
        // Verify the return type is a future (not a completed value)
        CompletableFuture<Report> future = reportService.generateReport(
            new ReportRequest("user-4", "ANNUAL", 365));

        assertThat(future).isNotNull();
        assertThat(future).isInstanceOf(CompletableFuture.class);
    }
}
