package com.example.fintech.capstone.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Capstone Exercise O — Async: Thread pool configuration
 *
 * TODO O1: Implement reportExecutor():
 *          - Create a ThreadPoolTaskExecutor
 *          - Set corePoolSize = 4
 *          - Set maxPoolSize = 8
 *          - Set queueCapacity = 50
 *          - Set threadNamePrefix = "report-"
 *          - Call executor.initialize() before returning
 */
@Configuration
public class AsyncConfig {

    @Bean("reportExecutor")
    public Executor reportExecutor() {
        // TODO O1: configure and return ThreadPoolTaskExecutor
        throw new UnsupportedOperationException("TODO O1: implement reportExecutor");
    }
}
