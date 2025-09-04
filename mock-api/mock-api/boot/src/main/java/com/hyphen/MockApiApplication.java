package com.hyphen;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.hyphen")
public class MockApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(MockApiApplication.class, args);
    }
}