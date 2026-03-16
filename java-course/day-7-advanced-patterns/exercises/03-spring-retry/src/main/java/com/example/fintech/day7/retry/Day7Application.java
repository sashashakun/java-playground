package com.example.fintech.day7.retry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class Day7Application {
    public static void main(String[] args) {
        SpringApplication.run(Day7Application.class, args);
    }
}
