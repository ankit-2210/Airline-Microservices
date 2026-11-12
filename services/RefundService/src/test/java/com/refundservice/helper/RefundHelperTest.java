package com.refundservice.helper;


import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.utils.Booking.BookingStatus;
import com.airlineportal.utils.Refund.RefundStatus;
import com.refundservice.model.Refund;
import com.refundservice.repository.RefundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RefundHelperTest {
    @Mock
    private RefundRepository refundRepository;

    @InjectMocks
    private RefundHelper refundHelper;

    private Refund refund;
    private BookingResponse booking;

    @BeforeEach
    void setUp() {
        refund = Refund.builder()
                .id(1L)
                .bookingId(100L)
                .userId(10L)
                .pnr("ABC123")
                .originalAmount(new BigDecimal("10000.00"))
                .cancellationFee(new BigDecimal("1000.00"))
                .refundAmount(new BigDecimal("9000.00"))
                .status(RefundStatus.REQUESTED)
                .build();

        booking = BookingResponse.builder()
                .id(100L)
                .userId(10L)
                .pnr("ABC123")
                .bookingStatus(String.valueOf(BookingStatus.CANCELLED))
                .totalAmount(new BigDecimal("10000.00"))
                .build();
    }

    // FIND REFUND BY ID
    @Test
    void findById_shouldReturnRefund() {
        when(refundRepository.findById(1L))
                .thenReturn(Optional.of(refund));

        Refund result = refundHelper.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(refundRepository).findById(1L);
    }

    @Test
    void findById_shouldThrowExceptionWhenRefundNotFound() {
        when(refundRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> refundHelper.findById(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Refund not found with ID: 1");
    }

    // FIND REFUND BY BOOKING ID
    @Test
    void findByBookingId_shouldReturnRefund() {
        when(refundRepository.findByBookingId(100L))
                .thenReturn(Optional.of(refund));

        Refund result = refundHelper.findByBookingId(100L);

        assertThat(result.getBookingId()).isEqualTo(100L);

        verify(refundRepository).findByBookingId(100L);
    }

    @Test
    void findByBookingId_shouldThrowExceptionWhenRefundNotFound() {
        when(refundRepository.findByBookingId(100L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> refundHelper.findByBookingId(100L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Refund not found for booking ID: 100");
    }


    // VALIDATE BOOKING FOR REFUND
    @Test
    void validateBookingForRefund_shouldPassForCancelledBooking() {
        refundHelper.validateBookingForRefund(booking);

        assertThat(booking.getBookingStatus())
                .isEqualTo("CANCELLED");
    }

    @Test
    void validateBookingForRefund_shouldThrowWhenBookingIsNull() {
        assertThatThrownBy(() ->
                refundHelper.validateBookingForRefund(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Booking not found");
    }

    @Test
    void validateBookingForRefund_shouldThrowWhenBookingStatusIsNull() {
        booking.setBookingStatus(null);

        assertThatThrownBy(() ->
                refundHelper.validateBookingForRefund(booking))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Booking status is missing");
    }

    @Test
    void validateBookingForRefund_shouldThrowWhenBookingIsNotCancelled() {
        booking.setBookingStatus("CONFIRMED");

        assertThatThrownBy(() ->
                refundHelper.validateBookingForRefund(booking))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Refund can only be created for a cancelled booking");
    }

    @Test
    void validateBookingForRefund_shouldThrowWhenAmountIsNull() {
        booking.setTotalAmount(null);

        assertThatThrownBy(() ->
                refundHelper.validateBookingForRefund(booking))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Booking amount must be greater than zero");
    }

    @Test
    void validateBookingForRefund_shouldThrowWhenAmountIsZero() {
        booking.setTotalAmount(BigDecimal.ZERO);

        assertThatThrownBy(() ->
                refundHelper.validateBookingForRefund(booking))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Booking amount must be greater than zero");
    }

    @Test
    void validateBookingForRefund_shouldThrowWhenAmountIsNegative() {
        booking.setTotalAmount(new BigDecimal("-100"));

        assertThatThrownBy(() ->
                refundHelper.validateBookingForRefund(booking))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Booking amount must be greater than zero");
    }

    // CALCULATE CANCELLATION FEE
    @Test
    void calculateCancellationFee_shouldCalculateTenPercent() {
        BigDecimal fee = refundHelper.calculateCancellationFee(new BigDecimal("10000.00"));
        assertThat(fee).isEqualByComparingTo("1000.00");
    }

    @Test
    void calculateCancellationFee_shouldRoundHalfUp() {
        BigDecimal fee = refundHelper.calculateCancellationFee(new BigDecimal("105.55"));
        assertThat(fee).isEqualByComparingTo("10.56");
    }

    @Test
    void calculateCancellationFee_shouldReturnZeroForSmallAmountWhenRounded() {
        BigDecimal fee = refundHelper.calculateCancellationFee(new BigDecimal("0.01"));
        assertThat(fee).isEqualByComparingTo("0.00");
    }

    @Test
    void calculateCancellationFee_shouldThrowWhenAmountIsNull() {
        assertThatThrownBy(() ->
                refundHelper.calculateCancellationFee(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Original amount must be greater than zero");
    }

    @Test
    void calculateCancellationFee_shouldThrowWhenAmountIsZero() {
        assertThatThrownBy(() ->
                refundHelper.calculateCancellationFee(BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Original amount must be greater than zero");
    }

    @Test
    void calculateCancellationFee_shouldThrowWhenAmountIsNegative() {
        assertThatThrownBy(() ->
                refundHelper.calculateCancellationFee(
                        new BigDecimal("-100")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Original amount must be greater than zero");
    }

    // CALCULATE REFUND AMOUNT
    @Test
    void calculateRefundAmount_shouldCalculateCorrectly() {
        BigDecimal result = refundHelper.calculateRefundAmount(
            new BigDecimal("10000.00"),
            new BigDecimal("1000.00")
        );

        assertThat(result).isEqualByComparingTo("9000.00");
    }

    @Test
    void calculateRefundAmount_shouldReturnZeroWhenFeeEqualsOriginalAmount() {
        BigDecimal result = refundHelper.calculateRefundAmount(
                new BigDecimal("1000.00"),
                new BigDecimal("1000.00")
        );

        assertThat(result).isEqualByComparingTo("0.00");
    }

    @Test
    void calculateRefundAmount_shouldThrowWhenOriginalAmountIsNull() {
        assertThatThrownBy(() ->
                refundHelper.calculateRefundAmount(
                        null,
                        new BigDecimal("100")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Original amount cannot be null");
    }

    @Test
    void calculateRefundAmount_shouldThrowWhenCancellationFeeIsNull() {
        assertThatThrownBy(() ->
                refundHelper.calculateRefundAmount(
                        new BigDecimal("1000"),
                        null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cancellation fee cannot be null");
    }

    @Test
    void calculateRefundAmount_shouldThrowWhenRefundAmountIsNegative() {
        assertThatThrownBy(() ->
                refundHelper.calculateRefundAmount(
                        new BigDecimal("1000"),
                        new BigDecimal("1200")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Refund amount cannot be negative");
    }

    // VALIDATE CAN PROCESS REFUND
    @Test
    void validateCanProcessRefund_shouldPassForRequestedRefund() {
        refundHelper.validateCanProcessRefund(refund);

        assertThat(refund.getStatus())
                .isEqualTo(RefundStatus.REQUESTED);
    }

    @Test
    void validateCanProcessRefund_shouldThrowWhenRefundIsNull() {
        assertThatThrownBy(() ->
                refundHelper.validateCanProcessRefund(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Refund not found");
    }

    @Test
    void validateCanProcessRefund_shouldThrowWhenStatusIsNotRequested() {
        refund.setStatus(RefundStatus.PROCESSING);

        assertThatThrownBy(() ->
                refundHelper.validateCanProcessRefund(refund))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Refund can only be processed when status is REQUESTED");
    }

    @Test
    void validateCanProcessRefund_shouldThrowWhenBookingIdIsNull() {
        refund.setBookingId(null);

        assertThatThrownBy(() ->
                refundHelper.validateCanProcessRefund(refund))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Booking ID is missing from refund");
    }

    @Test
    void validateCanProcessRefund_shouldThrowWhenRefundAmountIsNull() {
        refund.setRefundAmount(null);

        assertThatThrownBy(() ->
                refundHelper.validateCanProcessRefund(refund))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Refund amount must be greater than zero");
    }

    @Test
    void validateCanProcessRefund_shouldThrowWhenRefundAmountIsZero() {
        refund.setRefundAmount(BigDecimal.ZERO);

        assertThatThrownBy(() ->
                refundHelper.validateCanProcessRefund(refund))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Refund amount must be greater than zero");
    }

    @Test
    void validateCanProcessRefund_shouldThrowWhenRefundAmountIsNegative() {
        refund.setRefundAmount(new BigDecimal("-100"));

        assertThatThrownBy(() ->
                refundHelper.validateCanProcessRefund(refund))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Refund amount must be greater than zero");
    }


    // VALIDATE STATUS TRANSITIONS
    @Test
    void validateStatusTransition_shouldAllowRequestedToProcessing() {
        refundHelper.validateStatusTransition(RefundStatus.REQUESTED, RefundStatus.PROCESSING);
    }

    @Test
    void validateStatusTransition_shouldAllowRequestedToRejected() {
        refundHelper.validateStatusTransition(RefundStatus.REQUESTED, RefundStatus.REJECTED);
    }

    @Test
    void validateStatusTransition_shouldAllowProcessingToCompleted() {
        refundHelper.validateStatusTransition(RefundStatus.PROCESSING, RefundStatus.COMPLETED);
    }

    @Test
    void validateStatusTransition_shouldAllowProcessingToFailed() {
        refundHelper.validateStatusTransition(RefundStatus.PROCESSING, RefundStatus.FAILED);
    }

    @Test
    void validateStatusTransition_shouldAllowFailedToProcessing() {
        refundHelper.validateStatusTransition(RefundStatus.FAILED, RefundStatus.PROCESSING);
    }

    @Test
    void validateStatusTransition_shouldAllowFailedToRejected() {
        refundHelper.validateStatusTransition(RefundStatus.FAILED, RefundStatus.REJECTED);
    }

    @Test
    void validateStatusTransition_shouldThrowWhenCurrentStatusIsNull() {
        assertThatThrownBy(() ->
                refundHelper.validateStatusTransition(
                        null,
                        RefundStatus.PROCESSING))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Current refund status is missing");
    }

    @Test
    void validateStatusTransition_shouldThrowWhenNewStatusIsNull() {
        assertThatThrownBy(() ->
                refundHelper.validateStatusTransition(
                        RefundStatus.REQUESTED,
                        null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("New refund status cannot be null");
    }

    @Test
    void validateStatusTransition_shouldThrowWhenCompletedRefundIsModified() {
        assertThatThrownBy(() ->
                refundHelper.validateStatusTransition(
                        RefundStatus.COMPLETED,
                        RefundStatus.FAILED))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Completed refund cannot be modified");
    }

    @Test
    void validateStatusTransition_shouldThrowWhenRejectedRefundIsModified() {

        assertThatThrownBy(() ->
                refundHelper.validateStatusTransition(
                        RefundStatus.REJECTED,
                        RefundStatus.PROCESSING))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Rejected refund cannot be modified");
    }

    @Test
    void validateStatusTransition_shouldRejectInvalidRequestedTransition() {
        assertThatThrownBy(() ->
                refundHelper.validateStatusTransition(
                        RefundStatus.REQUESTED,
                        RefundStatus.COMPLETED))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Requested refund can only move to PROCESSING or REJECTED");
    }

    @Test
    void validateStatusTransition_shouldRejectInvalidProcessingTransition() {
        assertThatThrownBy(() ->
                refundHelper.validateStatusTransition(
                        RefundStatus.PROCESSING,
                        RefundStatus.REJECTED))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Processing refund can only move to COMPLETED or FAILED");
    }

    @Test
    void validateStatusTransition_shouldRejectInvalidFailedTransition() {
        assertThatThrownBy(() ->
                refundHelper.validateStatusTransition(
                        RefundStatus.FAILED,
                        RefundStatus.COMPLETED))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Failed refund can only be retried or rejected");
    }


}
