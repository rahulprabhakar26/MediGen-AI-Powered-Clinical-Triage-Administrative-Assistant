package com.medigen.payment.service;

import com.medigen.payment.dto.PaymentRequest;
import com.medigen.payment.dto.PaymentResponse;
// import com.medigen.payment.dto.RazorpayOrderRequest;
// import com.medigen.payment.dto.RazorpayOrderResponse;
import com.medigen.payment.model.Payment;
import com.medigen.payment.repository.PaymentRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    
    @Value("${razorpay.key.id}")
    private String razorpayKeyId;
    
    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;
    
    public PaymentResponse createPaymentOrder(PaymentRequest request) {
        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            
            // Create Razorpay order
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", request.getAmount().multiply(new BigDecimal("100")).intValue()); // Convert to paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "receipt_" + UUID.randomUUID().toString());
            
            com.razorpay.Order razorpayOrder = razorpayClient.orders.create(orderRequest);
            
            // Save payment record
            Payment payment = new Payment();
            payment.setPatientId(request.getPatientId());
            payment.setDoctorId(request.getDoctorId());
            payment.setAmount(request.getAmount());
            payment.setDescription(request.getDescription());
            payment.setRazorpayOrderId(razorpayOrder.get("id"));
            payment.setStatus(Payment.PaymentStatus.PENDING);
            payment.setCreatedAt(new Date());
            
            Payment savedPayment = paymentRepository.save(payment);
            
            PaymentResponse response = new PaymentResponse();
            response.setOrderId(razorpayOrder.get("id"));
            response.setAmount(request.getAmount());
            response.setCurrency("INR");
            response.setStatus("PENDING");
            response.setMessage("Payment order created successfully");
            
            return response;
            
        } catch (RazorpayException e) {
            throw new RuntimeException("Failed to create payment order: " + e.getMessage());
        }
    }
    
    public PaymentResponse verifyPayment(String paymentId, String razorpaySignature) {
        try {
            Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
            
            // In a real implementation, you would verify the signature here
            // For this prototype, we'll simulate successful verification
            
            payment.setPaymentId(paymentId);
            payment.setRazorpaySignature(razorpaySignature);
            payment.setStatus(Payment.PaymentStatus.COMPLETED);
            payment.setUpdatedAt(new Date());
            
            paymentRepository.save(payment);
            
            PaymentResponse response = new PaymentResponse();
            response.setPaymentId(paymentId);
            response.setOrderId(payment.getRazorpayOrderId());
            response.setAmount(payment.getAmount());
            response.setCurrency(payment.getCurrency());
            response.setStatus("COMPLETED");
            response.setMessage("Payment verified successfully");
            
            return response;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify payment: " + e.getMessage());
        }
    }
    
    public Map<String, Object> getPaymentHistory(Long patientId) {
        var payments = paymentRepository.findByPatientId(patientId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("payments", payments);
        response.put("totalPayments", payments.size());
        
        return response;
    }
    
    public Map<String, Object> getDoctorPayments(Long doctorId) {
        var payments = paymentRepository.findByDoctorId(doctorId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("payments", payments);
        response.put("totalPayments", payments.size());
        
        return response;
    }
}
