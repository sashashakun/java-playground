package com.example.fintech.day7.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Day7Application {
    public static void main(String[] args) {
        SpringApplication.run(Day7Application.class, args);
    }
}
