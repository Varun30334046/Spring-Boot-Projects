package com.example.studentcourseregistration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * StudentCourseRegistrationApplication
 *
 * The main entry point for the Spring Boot application.
 *
 * @SpringBootApplication is a convenience annotation that combines:
 * 1. @Configuration: Declares this class as a source of Spring beans.
 * 2. @EnableAutoConfiguration: Automatically configures Spring Boot based on jar dependencies.
 * 3. @ComponentScan: Scans for Spring components (@RestController, @Service, @Component)
 *    in this package and all its subpackages (model, controller, service, exception).
 */
@SpringBootApplication
public class StudentCourseRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentCourseRegistrationApplication.class, args);
        System.out.println("==================================================");
        System.out.println(" Student Course Registration System Started!     ");
        System.out.println(" Base URL: http://localhost:8080/api/students   ");
        System.out.println("==================================================");
    }
}
