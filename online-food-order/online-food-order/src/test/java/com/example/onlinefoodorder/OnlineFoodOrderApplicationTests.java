package com.example.onlinefoodorder;

import com.example.onlinefoodorder.exception.OrderNotFoundException;
import com.example.onlinefoodorder.model.Order;
import com.example.onlinefoodorder.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OnlineFoodOrderApplicationTests {

    @Autowired
    private OrderService orderService;

    @Test
    void contextLoads() {
        assertNotNull(orderService, "OrderService should be injected successfully into the application context");
    }

    @Test
    @DisplayName("Test Add and Retrieve Order")
    void testAddAndGetOrder() {
        Order newOrder = new Order(null, "Varun", "Chicken Biryani", 2, 250.0);
        Order savedOrder = orderService.addOrder(newOrder);

        assertNotNull(savedOrder.getOrderId(), "Order ID should be auto-generated");
        assertEquals("Varun", savedOrder.getCustomerName());
        assertEquals("Chicken Biryani", savedOrder.getFoodItem());
        assertEquals(2, savedOrder.getQuantity());
        assertEquals(250.0, savedOrder.getPrice());

        Order found = orderService.getOrderById(savedOrder.getOrderId());
        assertEquals(savedOrder.getOrderId(), found.getOrderId());
    }

    @Test
    @DisplayName("Test Order Not Found Exception")
    void testOrderNotFound() {
        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrderById(999999L);
        });
    }

    @Test
    @DisplayName("Test Full Update Order")
    void testUpdateOrder() {
        Order initial = orderService.addOrder(new Order(null, "Varun", "Paneer Butter Masala", 1, 180.0));
        Long id = initial.getOrderId();

        Order updateData = new Order(null, "Varun Urrinkala", "Butter Naan & Paneer", 3, 320.0);
        Order updated = orderService.updateOrder(id, updateData);

        assertEquals("Varun Urrinkala", updated.getCustomerName());
        assertEquals("Butter Naan & Paneer", updated.getFoodItem());
        assertEquals(3, updated.getQuantity());
        assertEquals(320.0, updated.getPrice());
    }

    @Test
    @DisplayName("Test Partial Update (PATCH)")
    void testPatchOrder() {
        Order initial = orderService.addOrder(new Order(null, "Ramesh", "Veg Fried Rice", 1, 150.0));
        Long id = initial.getOrderId();

        // Update ONLY quantity
        Order patchData = new Order();
        patchData.setQuantity(4);

        Order patched = orderService.patchOrder(id, patchData);

        assertEquals(4, patched.getQuantity());
        assertEquals("Ramesh", patched.getCustomerName()); // Preserved
        assertEquals("Veg Fried Rice", patched.getFoodItem()); // Preserved
        assertEquals(150.0, patched.getPrice()); // Preserved
    }

    @Test
    @DisplayName("Test Delete Order")
    void testDeleteOrder() {
        Order initial = orderService.addOrder(new Order(null, "Suresh", "Masala Dosa", 2, 90.0));
        Long id = initial.getOrderId();

        orderService.deleteOrder(id);

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOrderById(id);
        });
    }
}
