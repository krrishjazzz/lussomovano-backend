package com.lussomovano.backend.service;

import com.lussomovano.backend.entity.Order;
import com.lussomovano.backend.entity.User;

import java.util.List;

public interface OrderService {
    Order placeOrder(Order order, User user);
    List<Order> getUserOrders(Long userId);
}
