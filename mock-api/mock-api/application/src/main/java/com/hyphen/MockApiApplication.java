package com.hyphen;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.hyphen")
public class MockApiApplication {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}