package com.example.fintech.day7.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

// SOLUTION 04 — Async: AsyncConfig

@Configuration
public class AsyncConfig {

    @Bean("reportExecutor")
    public Executor reportExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);          // TODO 1a ✓
        executor.setMaxPoolSize(8);           // TODO 1b ✓
        executor.setQueueCapacity(100);       // TODO 1c ✓
        executor.setThreadNamePrefix("report-async-");  // TODO 1d ✓
        executor.initialize();               // TODO 1e ✓
        return executor;
    }
}
