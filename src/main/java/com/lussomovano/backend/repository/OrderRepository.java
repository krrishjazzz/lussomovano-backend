package com.lussomovano.backend.repository;

import com.lussomovano.backend.entity.Order;
import com.lussomovano.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Optional<Order> findByRazorpayOrderId(String razorpayOrderId);
}
