package com.medigen.payment.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String orderId;
    private String paymentId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String message;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class RazorpayOrderRequest {
    private BigDecimal amount;
    private String currency;
    private String receipt;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class RazorpayOrderResponse {
    private String id;
    private BigDecimal amount;
    private String currency;
    private String receipt;
    private String status;
}
