package com.example.onlinefoodorder.exception;

/**
 * Custom runtime exception thrown when an Order with a requested ID is not found.
 */
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String message) {
        super(message);
    }
}
