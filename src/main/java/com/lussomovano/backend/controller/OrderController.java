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

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody Order order, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        return ResponseEntity.ok(orderService.placeOrder(order, user));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<Order>> getMyOrders(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        return ResponseEntity.ok(orderService.getUserOrders(user.getId()));
    }
}
