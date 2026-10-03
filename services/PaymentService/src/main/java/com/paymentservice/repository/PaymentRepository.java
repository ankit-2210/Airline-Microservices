package com.paymentservice.repository;

import com.airlineportal.utils.Booking.PaymentStatus;
import com.paymentservice.model.Payment;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByBookingId(Long bookingId);
    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByRazorpayPaymentLinkId(String razorpayPaymentLinkId);

//    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);

    Page<Payment> findByUserId(Long userId, Pageable pageable);
    Page<Payment> findByPaymentStatus(PaymentStatus paymentStatus, Pageable pageable);

    boolean existsByBookingId(Long bookingId);

}
