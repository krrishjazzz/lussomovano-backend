// src/main/java/com/lussomovano/backend/service/EmailService.java
package com.lussomovano.backend.service;

public interface EmailService {
    void sendOrderConfirmationEmail(String to, String subject, String body);
}
