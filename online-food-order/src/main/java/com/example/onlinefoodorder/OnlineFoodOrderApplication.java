package com.example.onlinefoodorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application Entry Point
 * Starts the embedded Tomcat server and enables automatic component scanning
 * across all subpackages (model, controller, service, exception).
 */
@SpringBootApplication
public class OnlineFoodOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineFoodOrderApplication.class, args);
    }
}
