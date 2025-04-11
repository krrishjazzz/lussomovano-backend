package com.lussomovano.backend.service.impl;

import com.lussomovano.backend.entity.Order;
import com.lussomovano.backend.entity.OrderItem;
import com.lussomovano.backend.entity.Product;
import com.lussomovano.backend.entity.User;
import com.lussomovano.backend.repository.OrderRepository;
import com.lussomovano.backend.repository.ProductRepository;
import com.lussomovano.backend.service.EmailService;
import com.lussomovano.backend.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;

    // ✅ Place an order
    @Override
    public Order placeOrder(Order order, User user) {
        double total = 0.0;
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);

            item.setOrder(order);
            item.setProduct(product);
            item.setPrice(product.getPrice() * item.getQuantity());
            total += item.getPrice();
        }

        Order savedOrder = orderRepository.save(order);

        // ✉️ Send confirmation email
        String subject = "Your Lusso Movano Order Confirmation";
        String body = "Dear " + user.getUsername() + ",\n\nThank you for your order!" +
                "\nOrder ID: " + savedOrder.getId() +
                "\nTotal: ₹" + savedOrder.getTotalAmount() +
                "\n\nWe'll notify you when it's shipped.\n\n- Lusso Movano Team";

        emailService.sendOrderConfirmationEmail(user.getEmail(), subject, body);

        return savedOrder;
    }

    // ✅ Get orders of a user
    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }

    // ✅ Get all orders (Admin only)
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // ✅ Get order by ID (no auth check)
    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }

    // ✅ Cancel order if user is owner
    @Override
    public void cancelOrder(Long orderId, String email) {
        Order order = getOrderById(orderId);
        if (!order.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("You are not authorized to cancel this order.");
        }
        orderRepository.delete(order);
    }

    // ✅ Get order by ID with auth check
    @Override
    public Order getOrderByIdWithAuthorization(Long orderId, String email) {
        Order order = getOrderById(orderId);
        if (!order.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("You are not authorized to view this order.");
        }
        return order;
    }

    public Order getByRazorpayOrderId(String razorpayOrderId) {
        return orderRepository.findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
