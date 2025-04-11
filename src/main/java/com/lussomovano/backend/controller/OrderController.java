package com.lussomovano.backend.controller;

import com.lussomovano.backend.entity.Order;
import com.lussomovano.backend.entity.User;
import com.lussomovano.backend.service.OrderService;
import com.lussomovano.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // ✅ Place an order
    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody Order order, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Order placedOrder = orderService.placeOrder(order, user);
        return ResponseEntity.ok(placedOrder);
    }

    // ✅ View logged-in user's orders
    @GetMapping("/user")
    public ResponseEntity<List<Order>> getUserOrders(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        return ResponseEntity.ok(orderService.getOrdersByUser(user));
    }

    // ✅ Admin - view all orders
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // ✅ View specific order with auth check
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(orderService.getOrderByIdWithAuthorization(id, principal.getName()));
    }

    // ✅ Cancel order
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id, Principal principal) {
        orderService.cancelOrder(id, principal.getName());
        return ResponseEntity.ok("Order canceled successfully");
    }
}
