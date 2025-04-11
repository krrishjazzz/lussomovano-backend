package com.lussomovano.backend.service;

import com.lussomovano.backend.entity.Order;
import com.lussomovano.backend.entity.User;

import java.util.List;

public interface OrderService {
    Order placeOrder(Order order, User user);
    List<Order> getOrdersByUser(User user);
    List<Order> getAllOrders(); // Admin only
    Order getOrderById(Long id);
    public void cancelOrder(Long orderId, String username);
    Order getOrderByIdWithAuthorization(Long id, String username);
    Order getByRazorpayOrderId(String razorpayOrderId);
    Order save(Order order);

}
