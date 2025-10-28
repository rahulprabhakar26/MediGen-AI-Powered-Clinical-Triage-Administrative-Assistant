package com.medigen.payment.controller;

import com.medigen.payment.dto.PaymentRequest;
import com.medigen.payment.dto.PaymentResponse;
import com.medigen.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @PostMapping("/create-order")
    public ResponseEntity<PaymentResponse> createPaymentOrder(@Valid @RequestBody PaymentRequest request) {
        try {
            PaymentResponse response = paymentService.createPaymentOrder(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/verify")
    public ResponseEntity<PaymentResponse> verifyPayment(
            @RequestParam String paymentId,
            @RequestParam String razorpaySignature) {
        try {
            PaymentResponse response = paymentService.verifyPayment(paymentId, razorpaySignature);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Map<String, Object>> getPaymentHistory(@PathVariable Long patientId) {
        Map<String, Object> response = paymentService.getPaymentHistory(patientId);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Map<String, Object>> getDoctorPayments(@PathVariable Long doctorId) {
        Map<String, Object> response = paymentService.getDoctorPayments(doctorId);
        return ResponseEntity.ok(response);
    }
}
