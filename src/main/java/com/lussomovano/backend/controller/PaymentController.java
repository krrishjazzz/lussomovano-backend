package com.lussomovano.backend.controller;

import com.lussomovano.backend.entity.Order;
import com.lussomovano.backend.service.OrderService;
import com.lussomovano.backend.service.PaymentService;
import com.razorpay.RazorpayException;
//import com.razorpay.Order as RazorpayOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    // Step 1: Initiate payment
    @PostMapping("/create/{orderId}")
    public ResponseEntity<?> createOrder(@PathVariable Long orderId, Principal principal) {
        try {
            Order order = orderService.getOrderByIdWithAuthorization(orderId, principal.getName());
            com.razorpay.Order razorpayOrder = paymentService.createRazorpayOrder(order);

            Map<String, Object> response = new HashMap<>();
            response.put("orderId", razorpayOrder.get("id"));
            response.put("amount", razorpayOrder.get("amount"));
            response.put("currency", razorpayOrder.get("currency"));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to create Razorpay order: " + e.getMessage());
        }
    }

    // Step 2: Verify signature
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> payload) throws Exception {
        String razorpayOrderId = payload.get("razorpay_order_id");
        String paymentId = payload.get("razorpay_payment_id");
        String signature = payload.get("razorpay_signature");

        Order order = orderService.getByRazorpayOrderId(razorpayOrderId);

        if (paymentService.verifyPaymentSignature(razorpayOrderId, paymentId, signature)) {
            order.setStatus("PAID");
            order.setRazorpayPaymentId(paymentId);
        } else {
            order.setStatus("FAILED");
        }

        orderService.save(order);
        return ResponseEntity.ok(Map.of("status", order.getStatus()));
    }
}

