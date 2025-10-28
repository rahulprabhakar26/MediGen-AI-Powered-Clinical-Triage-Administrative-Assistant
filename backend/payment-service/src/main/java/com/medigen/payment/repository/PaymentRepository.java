package com.medigen.payment.repository;

import com.medigen.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentId(String paymentId);
    List<Payment> findByPatientId(Long patientId);
    List<Payment> findByDoctorId(Long doctorId);
    List<Payment> findByStatus(Payment.PaymentStatus status);
}
