package com.refundservice.mapper;


import com.airlineportal.payload.request.Payment.PaymentRequest;
import com.airlineportal.payload.request.Refund.RefundCreateRequest;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.Refund.RefundResponse;
import com.airlineportal.utils.Refund.RefundStatus;
import com.refundservice.dto.RefundReportData;
import com.refundservice.model.Refund;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class RefundMapper {
    private RefundMapper() {

    }

    public static Refund toEntity(RefundCreateRequest request, BookingResponse booking, BigDecimal originalAmount, BigDecimal cancellationFee, BigDecimal refundAmount){

        return Refund.builder()
                .bookingId(booking.getId())
                .userId(booking.getUserId())

                .pnr(booking.getPnr())

                .originalAmount(originalAmount)
                .cancellationFee(cancellationFee)
                .refundAmount(refundAmount)

                .reason(request.getReason())
                .remarks(request.getRemarks())
                .status(RefundStatus.REQUESTED)

                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }


    public static RefundResponse toResponse(Refund refund) {
        if(refund == null)
            return null;

        return RefundResponse.builder()
                .id(refund.getId())
                .bookingId(refund.getBookingId())
                .userId(refund.getUserId())
                .paymentId(refund.getPaymentId())

                .pnr(refund.getPnr())
                .originalAmount(refund.getOriginalAmount())
                .cancellationFee(refund.getCancellationFee())

                .refundAmount(refund.getRefundAmount())
                .reason(refund.getReason())
                .status(refund.getStatus())

                .gatewayRefundId(refund.getGatewayRefundId())
                .remarks(refund.getRemarks())

                .createdAt(refund.getCreatedAt())
                .updatedAt(refund.getUpdatedAt())
                .build();

    }

    public static RefundReportData toReportData(Refund refund) {
        if (refund == null)
            return null;

        return RefundReportData.builder()
                .id(refund.getId())
                .bookingId(refund.getBookingId())
                .userId(refund.getUserId())

                .pnr(refund.getPnr())
                .originalAmount(refund.getOriginalAmount())
                .cancellationFee(refund.getCancellationFee())

                .refundAmount(refund.getRefundAmount())

                .reason(refund.getReason() != null ? refund.getReason().name() : null)
                .status(refund.getStatus() != null ? refund.getStatus().name() : null)

                .gatewayRefundId(refund.getGatewayRefundId())
                .remarks(refund.getRemarks())

                .createdAt(refund.getCreatedAt())
                .updatedAt(refund.getUpdatedAt())
                .build();
    }




}
