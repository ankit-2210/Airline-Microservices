package com.refundservice.model;


import java.math.BigDecimal;

import com.airlineportal.utils.Refund.RefundReason;
import com.airlineportal.utils.Refund.RefundStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


public class RefundTest {
    @Test
    void onCreate_shouldSetCreatedAtUpdatedAtAndDefaultStatus() {

        Refund refund = Refund.builder()
                .bookingId(100L)
                .userId(10L)
                .pnr("ABC123")
                .originalAmount(new BigDecimal("10000.00"))
                .cancellationFee(new BigDecimal("1000.00"))
                .refundAmount(new BigDecimal("9000.00"))
                .reason(RefundReason.BOOKING_CANCELLED)
                .build();

        assertThat(refund.getCreatedAt())
                .isNull();

        assertThat(refund.getUpdatedAt())
                .isNull();

        assertThat(refund.getStatus())
                .isNull();

        refund.onCreate();

        assertThat(refund.getCreatedAt())
                .isNotNull();

        assertThat(refund.getUpdatedAt())
                .isNotNull();

        assertThat(refund.getStatus())
                .isEqualTo(RefundStatus.REQUESTED);
    }

    @Test
    void onCreate_shouldNotOverwriteExistingCreatedAt() {

        LocalDateTime existingCreatedAt = LocalDateTime.of(2026, 1, 1, 10, 0);

        Refund refund = Refund.builder()
                .bookingId(100L)
                .userId(10L)
                .pnr("ABC123")
                .originalAmount(new BigDecimal("10000.00"))
                .cancellationFee(new BigDecimal("1000.00"))
                .refundAmount(new BigDecimal("9000.00"))
                .reason(RefundReason.BOOKING_CANCELLED)
                .createdAt(existingCreatedAt)
                .build();

        refund.onCreate();

        assertThat(refund.getCreatedAt())
                .isEqualTo(existingCreatedAt);
        assertThat(refund.getUpdatedAt())
                .isNotNull();
        assertThat(refund.getStatus())
                .isEqualTo(RefundStatus.REQUESTED);
    }

    @Test
    void onCreate_shouldNotOverwriteExistingStatus() {

        Refund refund = Refund.builder()
                .bookingId(100L)
                .userId(10L)
                .pnr("ABC123")
                .originalAmount(new BigDecimal("10000.00"))
                .cancellationFee(new BigDecimal("1000.00"))
                .refundAmount(new BigDecimal("9000.00"))
                .reason(RefundReason.BOOKING_CANCELLED)
                .status(RefundStatus.PROCESSING)
                .build();

        refund.onCreate();

        assertThat(refund.getStatus())
                .isEqualTo(RefundStatus.PROCESSING);
    }

    @Test
    void onUpdate_shouldUpdateUpdatedAt() {
        LocalDateTime oldUpdatedAt = LocalDateTime.of(2026, 1, 1, 10, 0);

        Refund refund = Refund.builder()
                .bookingId(100L)
                .userId(10L)
                .pnr("ABC123")
                .originalAmount(new BigDecimal("10000.00"))
                .cancellationFee(new BigDecimal("1000.00"))
                .refundAmount(new BigDecimal("9000.00"))
                .reason(RefundReason.BOOKING_CANCELLED)
                .status(RefundStatus.PROCESSING)
                .updatedAt(oldUpdatedAt)
                .build();

        refund.onUpdate();

        assertThat(refund.getUpdatedAt())
                .isNotNull();

        assertThat(refund.getUpdatedAt())
                .isAfter(oldUpdatedAt);
    }



}
