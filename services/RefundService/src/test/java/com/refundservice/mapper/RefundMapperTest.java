package com.refundservice.mapper;

import com.airlineportal.payload.request.Refund.RefundCreateRequest;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.Refund.RefundResponse;
import com.airlineportal.utils.Refund.RefundReason;
import com.airlineportal.utils.Refund.RefundStatus;
import com.refundservice.model.Refund;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


public class RefundMapperTest {
    @Test
    void toEntity_shouldMapRefundCreateRequestToEntity() {
        RefundCreateRequest request = RefundCreateRequest.builder()
                        .bookingId(100L)
                        .reason(RefundReason.BOOKING_CANCELLED)
                        .remarks("Customer cancelled")
                        .build();

        BookingResponse booking = BookingResponse.builder()
                        .id(100L)
                        .userId(10L)
                        .pnr("ABC123")
                        .bookingStatus("CANCELLED")
                        .totalAmount(new BigDecimal("10000.00"))
                        .build();

        BigDecimal originalAmount = new BigDecimal("10000.00");
        BigDecimal cancellationFee = new BigDecimal("1000.00");
        BigDecimal refundAmount = new BigDecimal("9000.00");

        Refund refund = RefundMapper.toEntity(request, booking, originalAmount, cancellationFee, refundAmount);

        assertThat(refund).isNotNull();
        assertThat(refund.getBookingId()).isEqualTo(100L);
        assertThat(refund.getUserId()).isEqualTo(10L);
        assertThat(refund.getPnr()).isEqualTo("ABC123");

        assertThat(refund.getOriginalAmount()).isEqualByComparingTo("10000.00");
        assertThat(refund.getCancellationFee()).isEqualByComparingTo("1000.00");
        assertThat(refund.getRefundAmount()).isEqualByComparingTo("9000.00");

        assertThat(refund.getReason()).isEqualTo(RefundReason.BOOKING_CANCELLED);
        assertThat(refund.getStatus()).isEqualTo(RefundStatus.REQUESTED);
        assertThat(refund.getRemarks()).isEqualTo("Customer cancelled");

        assertThat(refund.getCreatedAt()).isNotNull();
        assertThat(refund.getUpdatedAt()).isNotNull();
    }

    @Test
    void toResponse_shouldMapEntityToResponse() {
        Refund refund = Refund.builder()
                .id(1L)
                .bookingId(100L)
                .userId(10L)
                .paymentId(500L)
                .pnr("ABC123")
                .originalAmount(new BigDecimal("10000.00"))
                .cancellationFee(new BigDecimal("1000.00"))
                .refundAmount(new BigDecimal("9000.00"))
                .reason(RefundReason.BOOKING_CANCELLED)
                .status(RefundStatus.REQUESTED)
                .gatewayRefundId("rfnd_123")
                .remarks("Customer cancelled")
                .build();

        RefundResponse response = RefundMapper.toResponse(refund);

        assertThat(response).isNotNull();

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getBookingId()).isEqualTo(100L);
        assertThat(response.getUserId()).isEqualTo(10L);
        assertThat(response.getPaymentId()).isEqualTo(500L);
        assertThat(response.getPnr()).isEqualTo("ABC123");

        assertThat(response.getOriginalAmount()).isEqualByComparingTo("10000.00");
        assertThat(response.getCancellationFee()).isEqualByComparingTo("1000.00");
        assertThat(response.getRefundAmount()).isEqualByComparingTo("9000.00");

        assertThat(response.getReason()).isEqualTo(RefundReason.BOOKING_CANCELLED);
        assertThat(response.getStatus()).isEqualTo(RefundStatus.REQUESTED);
        assertThat(response.getGatewayRefundId()).isEqualTo("rfnd_123");

        assertThat(response.getRemarks()).isEqualTo("Customer cancelled");
    }




}
