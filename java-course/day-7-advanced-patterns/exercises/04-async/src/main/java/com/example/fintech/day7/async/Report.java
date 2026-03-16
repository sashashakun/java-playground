package com.example.fintech.day7.async;

import java.time.Instant;

public record Report(
    String reportId,
    String userId,
    String reportType,
    String content,
    String generatedByThread,
    Instant generatedAt
) {}
