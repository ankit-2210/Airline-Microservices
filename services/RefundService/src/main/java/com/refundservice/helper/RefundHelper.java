package com.refundservice.helper;


import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.utils.Booking.BookingStatus;
import com.airlineportal.utils.Refund.RefundStatus;
import com.refundservice.model.Refund;
import com.refundservice.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.*;

@Component
@RequiredArgsConstructor
public class RefundHelper {
    private final RefundRepository refundRepository;

    public Refund findById(Long refundId) {
        return refundRepository.findById(refundId)
                .orElseThrow(() -> new IllegalArgumentException("Refund not found with ID: " + refundId));
    }

    public Refund findByBookingId(Long bookingId) {
        return refundRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Refund not found for booking ID: " + bookingId));
    }

    public void validateBookingForRefund(BookingResponse booking){
        if(booking == null){
            throw new IllegalArgumentException("Booking not found");
        }
        if(booking.getBookingStatus() == null){
            throw new IllegalStateException("Booking status is missing");
        }
        if (!booking.getBookingStatus().equals(String.valueOf(BookingStatus.CANCELLED))) {
            throw new IllegalStateException("Refund can only be created for a cancelled booking");
        }
        if (booking.getTotalAmount() == null || booking.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Booking amount must be greater than zero");
        }
    }

    public BigDecimal calculateCancellationFee(BigDecimal originalAmount){
        if(originalAmount == null || originalAmount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Original amount must be greater than zero");
        }

        /*
         * Current business rule:
         * Cancellation fee = 10% of booking amount.
         */
        return originalAmount.multiply(BigDecimal.valueOf(0.10))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateRefundAmount(BigDecimal originalAmount, BigDecimal cancellationFee){
        if(originalAmount == null){
            throw new IllegalArgumentException("Original amount cannot be null");
        }
        if(cancellationFee == null){
            throw new IllegalArgumentException("Cancellation fee cannot be null");
        }

        BigDecimal refundAmount = originalAmount.subtract(cancellationFee);
        if(refundAmount.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalStateException("Refund amount cannot be negative");
        }

        return refundAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public void validateCanProcessRefund(
            Refund refund) {

        if (refund == null) {
            throw new IllegalArgumentException(
                    "Refund not found"
            );
        }

        if (refund.getStatus() != RefundStatus.REQUESTED) {
            throw new IllegalStateException(
                    "Refund can only be processed when status is REQUESTED"
            );
        }

        if (refund.getBookingId() == null) {
            throw new IllegalStateException(
                    "Booking ID is missing from refund"
            );
        }

        if (refund.getRefundAmount() == null
                || refund.getRefundAmount()
                .compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalStateException(
                    "Refund amount must be greater than zero"
            );
        }
    }


    public void validateStatusTransition(RefundStatus currentStatus, RefundStatus newStatus){
        if(currentStatus == null){
            throw new IllegalStateException("Current refund status is missing");
        }
        if(newStatus == null){
            throw new IllegalArgumentException("New refund status cannot be null");
        }
        if(currentStatus == RefundStatus.COMPLETED){
            throw new IllegalStateException("Completed refund cannot be modified");
        }
        if(currentStatus == RefundStatus.REJECTED){
            throw new IllegalStateException("Rejected refund cannot be modified");
        }
        if(currentStatus == RefundStatus.FAILED && newStatus != RefundStatus.PROCESSING && newStatus != RefundStatus.REJECTED) {
            throw new IllegalStateException("Failed refund can only be retried or rejected");
        }

        if(currentStatus == RefundStatus.REQUESTED && newStatus != RefundStatus.PROCESSING && newStatus != RefundStatus.REJECTED){
            throw new IllegalStateException("Requested refund can only move to PROCESSING or REJECTED");
        }
        if(currentStatus == RefundStatus.PROCESSING && newStatus != RefundStatus.COMPLETED && newStatus != RefundStatus.FAILED){
            throw new IllegalStateException("Processing refund can only move to COMPLETED or FAILED");
        }

    }


}


//REQUESTED
//    ├──→ PROCESSING
//    │       ├──→ COMPLETED
//    │       └──→ FAILED
//    │               ├──→ PROCESSING
//    │               └──→ REJECTED
//    │
//            └──→ REJECTED
