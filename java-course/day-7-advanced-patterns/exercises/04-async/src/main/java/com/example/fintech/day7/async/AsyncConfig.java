package com.example.fintech.day7.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Exercise 04 — Async Processing: Thread Pool Configuration
 *
 * @Async by default uses a single-thread SimpleAsyncTaskExecutor which is NOT
 * production-appropriate (creates a new thread per invocation).
 *
 * Best practice: define a named ThreadPoolTaskExecutor and reference it by name
 * in @Async("reportExecutor").
 *
 * TypeScript analogy: configuring a BullMQ worker pool or piscina thread pool.
 *
 * TODO 1: Configure the ThreadPoolTaskExecutor:
 *         - corePoolSize: 4      (keep 4 threads alive even when idle)
 *         - maxPoolSize: 8       (scale up to 8 under load)
 *         - queueCapacity: 100   (queue up to 100 tasks before rejecting)
 *         - threadNamePrefix: "report-async-"
 *         - call executor.initialize() before returning
 */
@Configuration
public class AsyncConfig {

    @Bean("reportExecutor")
    public Executor reportExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // TODO 1: configure the executor (corePoolSize, maxPoolSize, queueCapacity, threadNamePrefix)
        // TODO 1: executor.initialize();
        return executor;
    }
}
