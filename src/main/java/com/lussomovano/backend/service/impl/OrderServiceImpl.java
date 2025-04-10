package com.lussomovano.backend.service.impl;

import com.lussomovano.backend.entity.Order;
import com.lussomovano.backend.entity.OrderItem;
import com.lussomovano.backend.entity.User;
import com.lussomovano.backend.repository.OrderRepository;
import com.lussomovano.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order placeOrder(Order order, User user) {
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        // Set order for each item
        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
        }

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
