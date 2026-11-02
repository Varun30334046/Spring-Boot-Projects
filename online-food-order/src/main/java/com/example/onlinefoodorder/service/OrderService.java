package com.example.onlinefoodorder.service;

import com.example.onlinefoodorder.exception.OrderNotFoundException;
import com.example.onlinefoodorder.model.Order;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * OrderService
 * Business logic layer for managing food orders.
 * Stores orders in-memory using an ArrayList (no database).
 */
@Service
public class OrderService {

    // In-memory list to temporarily hold food orders
    private final List<Order> orderList = new ArrayList<>();

    // Counter to generate unique sequential order IDs starting from 1
    private final AtomicLong idCounter = new AtomicLong(1);

    // Jakarta Validator for validating orders programmatically (e.g., during PATCH)
    private final Validator validator;

    @Autowired
    public OrderService(Validator validator) {
        this.validator = validator;
    }

    /**
     * Operation A: GET ALL ORDERS
     * Returns a copy of all current orders in memory.
     */
    public synchronized List<Order> getAllOrders() {
        return new ArrayList<>(orderList);
    }

    /**
     * Operation B: GET ORDER BY ID
     * Searches for an order with the given ID.
     * Throws OrderNotFoundException if no matching order is found.
     */
    public synchronized Order getOrderById(Long id) {
        for (Order order : orderList) {
            if (order.getOrderId().equals(id)) {
                return order;
            }
        }
        throw new OrderNotFoundException("Order not found with ID: " + id);
    }

    /**
     * Operation C: ADD NEW ORDER
     * Automatically assigns a new unique orderId, adds order to the list, and returns it.
     */
    public synchronized Order addOrder(Order order) {
        // Auto-generate order ID
        Long newId = idCounter.getAndIncrement();
        order.setOrderId(newId);

        // Add to our in-memory list
        orderList.add(order);
        return order;
    }

    /**
     * Operation D: UPDATE ORDER (Full Update - PUT)
     * Replaces all editable fields of an existing order.
     */
    public synchronized Order updateOrder(Long id, Order updatedOrder) {
        // Retrieve existing order (will throw OrderNotFoundException if ID doesn't exist)
        Order existingOrder = getOrderById(id);

        // Update all editable fields
        existingOrder.setCustomerName(updatedOrder.getCustomerName());
        existingOrder.setFoodItem(updatedOrder.getFoodItem());
        existingOrder.setQuantity(updatedOrder.getQuantity());
        existingOrder.setPrice(updatedOrder.getPrice());

        return existingOrder;
    }

    /**
     * Operation E: DELETE ORDER
     * Removes the order with the given ID from memory.
     */
    public synchronized void deleteOrder(Long id) {
        // Ensure the order exists before deleting
        Order existingOrder = getOrderById(id);
        orderList.remove(existingOrder);
    }

    /**
     * Operation F: PARTIAL UPDATE (PATCH)
     * Updates only fields provided in the request without overwriting omitted fields.
     * Validates the final updated order before saving changes.
     */
    public synchronized Order patchOrder(Long id, Order patchData) {
        // Find existing order
        Order existingOrder = getOrderById(id);

        // Create a temporary clone with merged values to validate before modifying existing data
        Order mergedOrder = new Order(
                existingOrder.getOrderId(),
                patchData.getCustomerName() != null ? patchData.getCustomerName() : existingOrder.getCustomerName(),
                patchData.getFoodItem() != null ? patchData.getFoodItem() : existingOrder.getFoodItem(),
                patchData.getQuantity() != null ? patchData.getQuantity() : existingOrder.getQuantity(),
                patchData.getPrice() != null ? patchData.getPrice() : existingOrder.getPrice()
        );

        // Validate the final updated order object
        Set<ConstraintViolation<Order>> violations = validator.validate(mergedOrder);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        // Apply changes to the stored order once validation passes
        if (patchData.getCustomerName() != null) {
            existingOrder.setCustomerName(patchData.getCustomerName());
        }
        if (patchData.getFoodItem() != null) {
            existingOrder.setFoodItem(patchData.getFoodItem());
        }
        if (patchData.getQuantity() != null) {
            existingOrder.setQuantity(patchData.getQuantity());
        }
        if (patchData.getPrice() != null) {
            existingOrder.setPrice(patchData.getPrice());
        }

        return existingOrder;
    }
}
