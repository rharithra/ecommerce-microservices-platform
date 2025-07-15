package com.ecommerce.payment.service;

import com.ecommerce.payment.entity.Payment;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

@Service
public class RazorpayService {

    private static final Logger logger = LoggerFactory.getLogger(RazorpayService.class);

    @Value("${razorpay.key-id}")
    private String keyId;

    @Value("${razorpay.key-secret}")
    private String keySecret;

    private RazorpayClient razorpayClient;

    public RazorpayService() {
        try {
            this.razorpayClient = new RazorpayClient(keyId, keySecret);
        } catch (RazorpayException e) {
            logger.error("Error initializing Razorpay client: {}", e.getMessage());
        }
    }

    public Order createOrder(BigDecimal amount, String currency, String receipt) throws RazorpayException {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount.multiply(new BigDecimal("100")).intValue()); // amount in paise
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", receipt);
        orderRequest.put("payment_capture", 1); // auto capture payment

        return razorpayClient.orders.create(orderRequest);
    }

    public com.razorpay.Payment capturePayment(String paymentId, BigDecimal amount) throws RazorpayException {
        JSONObject captureRequest = new JSONObject();
        captureRequest.put("amount", amount.multiply(new BigDecimal("100")).intValue()); // amount in paise
        captureRequest.put("currency", "INR");

        return razorpayClient.payments.capture(paymentId, captureRequest);
    }

    public com.razorpay.Payment fetchPayment(String paymentId) throws RazorpayException {
        return razorpayClient.payments.fetch(paymentId);
    }

    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) {
        try {
            String payload = orderId + "|" + paymentId;
            String expectedSignature = calculateHmacSHA256(payload, keySecret);
            return expectedSignature.equals(signature);
        } catch (Exception e) {
            logger.error("Error verifying payment signature: {}", e.getMessage());
            return false;
        }
    }

    public com.razorpay.Refund createRefund(String paymentId, BigDecimal amount) throws RazorpayException {
        JSONObject refundRequest = new JSONObject();
        refundRequest.put("amount", amount.multiply(new BigDecimal("100")).intValue()); // amount in paise

        return razorpayClient.payments.refund(paymentId, refundRequest);
    }

    public Order fetchOrder(String orderId) throws RazorpayException {
        return razorpayClient.orders.fetch(orderId);
    }

    private String calculateHmacSHA256(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(data.getBytes());
        
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public boolean validateWebhookSignature(String payload, String signature, String secret) {
        try {
            return Utils.verifyWebhookSignature(payload, signature, secret);
        } catch (RazorpayException e) {
            logger.error("Error validating webhook signature: {}", e.getMessage());
            return false;
        }
    }
} 