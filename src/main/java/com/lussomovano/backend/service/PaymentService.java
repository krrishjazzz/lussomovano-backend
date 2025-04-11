package com.lussomovano.backend.service;

import com.lussomovano.backend.entity.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Service
public class PaymentService {

    @Value("${razorpay.key}")
    private String keyId;

    @Value("${razorpay.secret}")
    private String secret;

    public com.razorpay.Order createRazorpayOrder(Order order) throws RazorpayException {
        RazorpayClient client = new RazorpayClient(keyId, secret);

        JSONObject options = new JSONObject();
        options.put("amount", (int)(order.getTotalAmount() * 100)); // amount in paise
        options.put("currency", "INR");
        options.put("receipt", "order_rcptid_" + order.getId());
        options.put("payment_capture", 1);

        return client.orders.create(options); // returns com.razorpay.Order
    }

    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) throws Exception {
        String data = orderId + "|" + paymentId;

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secretKey);

        byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        String generatedSignature = Hex.encodeHexString(hash);

        return generatedSignature.equals(signature);
    }
}

