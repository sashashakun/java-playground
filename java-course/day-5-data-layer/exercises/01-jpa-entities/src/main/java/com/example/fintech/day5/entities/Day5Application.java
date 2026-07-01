package com.example.fintech.day5.entities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Day5Application {
    public static void main(String[] args) {
        SpringApplication.run(Day5Application.class, args);
    }
}
