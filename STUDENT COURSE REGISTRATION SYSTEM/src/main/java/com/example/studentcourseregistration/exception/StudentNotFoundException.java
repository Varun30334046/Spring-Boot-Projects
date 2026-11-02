package com.example.studentcourseregistration.exception;

/**
 * Custom exception thrown when a student with the specified ID
 * does not exist in the registration list.
 */
public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String message) {
        super(message);
    }
}
