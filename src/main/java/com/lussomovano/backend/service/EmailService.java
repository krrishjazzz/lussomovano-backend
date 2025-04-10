package com.lussomovano.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderConfirmation(String to, String orderDetails) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Lusso Movano Order Confirmation");
        message.setText(orderDetails);
        mailSender.send(message);
    }

    public void sendWelcomeEmail(String to, String name) {
        String welcomeMsg = "Hi " + name + ",\n\nWelcome to Lusso Movano! We're excited to have you.\n\n— The LM Team";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Welcome to Lusso Movano");
        message.setText(welcomeMsg);
        mailSender.send(message);
    }
}
