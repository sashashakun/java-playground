package com.example.fintech.day4.configprops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan  // registers @ConfigurationProperties beans
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
