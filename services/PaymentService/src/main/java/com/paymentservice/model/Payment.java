package com.paymentservice.model;

import com.airlineportal.utils.Booking.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "payments",
        indexes = {
                @Index(name = "idx_payment_booking_id", columnList = "booking_id"),
                @Index(name = "idx_payment_user_id", columnList = "user_id"),
                @Index(name = "idx_payment_razorpay_order_id", columnList = "razorpay_order_id"),
                @Index(name = "idx_payment_razorpay_payment_id", columnList = "razorpay_payment_id"),
                @Index(name = "idx_payment_status", columnList = "payment_status")
        }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Payment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @EqualsAndHashCode.Include
        private Long id;

        @Column(name = "booking_id", nullable = false, unique = true)
        private Long bookingId;

        @Column(name = "user_id", nullable = false)
        private Long userId;

        @Column(name = "pnr", nullable = false, length = 10)
        private String pnr;

        @Column(name = "amount", nullable = false, precision = 12, scale = 2)
        private BigDecimal amount;

        @Enumerated(EnumType.STRING)
        @Column(name = "payment_status", nullable = false, length = 30)
        @Builder.Default
        private PaymentStatus paymentStatus = PaymentStatus.PENDING;

        @Column(name = "payment_method", length = 30)
        private String paymentMethod;

        @Column(name = "transaction_id", unique = true, length = 100)
        private String transactionId;

        @Column(name = "razorpay_order_id", unique = true, length = 100)
        private String razorpayOrderId;


        @Column(name = "razorpay_payment_link_id", unique = true, length = 100)
        private String razorpayPaymentLinkId;

        @Column(name = "razorpay_payment_link_url", length = 500)
        private String razorpayPaymentLinkUrl;


        @Column(name = "razorpay_payment_id", unique = true, length = 100)
        private String razorpayPaymentId;

        @Column(name = "razorpay_signature", length = 500)
        private String razorpaySignature;

        @Column(name = "paid_at")
        private LocalDateTime paidAt;

        @Column(name = "created_at", nullable = false)
        @Builder.Default
        private LocalDateTime createdAt = LocalDateTime.now();

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;


}
