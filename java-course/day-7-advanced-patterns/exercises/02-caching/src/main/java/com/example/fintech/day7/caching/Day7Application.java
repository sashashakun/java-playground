package com.example.fintech.day7.caching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Day7Application {
    public static void main(String[] args) {
        SpringApplication.run(Day7Application.class, args);
    }
}
