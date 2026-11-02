package com.example.onlinefoodorder.controller;

import com.example.onlinefoodorder.model.Order;
import com.example.onlinefoodorder.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OrderController
 * REST Controller exposing the 6 HTTP endpoints for the Online Food Order System.
 * Delegates business logic and data storage to OrderService.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * A. GET ALL ORDERS
     * Method: GET
     * Endpoint: /api/orders
     * Function: Returns all food orders currently stored in memory.
     * Response: HTTP 200 OK with JSON array of orders.
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * B. GET ORDER BY ID
     * Method: GET
     * Endpoint: /api/orders/{id}
     * Function: Searches for a specific order by its orderId.
     * Response: HTTP 200 OK with Order JSON, or HTTP 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * C. ADD NEW ORDER
     * Method: POST
     * Endpoint: /api/orders
     * Function: Adds a new food order with an auto-generated orderId.
     * Response: HTTP 201 Created with the saved Order JSON.
     */
    @PostMapping
    public ResponseEntity<Order> addOrder(@Valid @RequestBody Order order) {
        Order savedOrder = orderService.addOrder(order);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    /**
     * D. UPDATE ORDER (Full Update)
     * Method: PUT
     * Endpoint: /api/orders/{id}
     * Function: Updates all editable fields of an existing order.
     * Response: HTTP 200 OK with updated Order JSON, or HTTP 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @Valid @RequestBody Order order) {
        Order updatedOrder = orderService.updateOrder(id, order);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * E. DELETE ORDER
     * Method: DELETE
     * Endpoint: /api/orders/{id}
     * Function: Deletes the order with the specified ID.
     * Response: HTTP 200 OK with success confirmation message, or HTTP 404 if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Order with ID " + id + " has been deleted successfully.");
        return ResponseEntity.ok(response);
    }

    /**
     * F. PARTIAL UPDATE (PATCH)
     * Method: PATCH
     * Endpoint: /api/orders/{id}
     * Function: Updates only the fields provided by the client without overwriting omitted fields.
     * Response: HTTP 200 OK with updated Order JSON, or HTTP 404 / 400 on error.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Order> patchOrder(@PathVariable Long id, @RequestBody Order patchData) {
        Order patchedOrder = orderService.patchOrder(id, patchData);
        return ResponseEntity.ok(patchedOrder);
    }
}
