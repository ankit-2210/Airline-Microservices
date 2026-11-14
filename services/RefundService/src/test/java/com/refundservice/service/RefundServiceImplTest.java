package com.refundservice.service;


import com.airlineportal.payload.request.Payment.PaymentRefundRequest;
import com.airlineportal.payload.request.Refund.RefundCreateRequest;
import com.airlineportal.payload.request.Refund.RefundStatusUpdateRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.Payment.PaymentRefundResponse;
import com.airlineportal.payload.response.Refund.RefundResponse;
import com.airlineportal.utils.Refund.RefundReason;
import com.airlineportal.utils.Refund.RefundStatus;
import com.refundservice.external.ExternalService;
import com.refundservice.helper.RefundHelper;
import com.refundservice.model.Refund;
import com.refundservice.repository.RefundRepository;
import com.refundservice.service.Impl.RefundServiceImpl;
import com.refundservice.service.RefundService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefundServiceImplTest {
    @Mock
    private RefundRepository refundRepository;

    @Mock
    private RefundHelper refundHelper;

    @Mock
    private ExternalService externalService;

    @InjectMocks
    private RefundServiceImpl refundService;

    private Refund refund;
    private BookingResponse booking;

    @BeforeEach
    void setUp() {
        booking = BookingResponse.builder()
                .id(100L)
                .userId(10L)
                .pnr("ABC123")
                .bookingStatus("CANCELLED")
                .totalAmount(new BigDecimal("10000.00"))
                .build();

        refund = Refund.builder()
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
                .gatewayRefundId(null)
                .remarks("Customer cancelled booking")
                .build();
    }

    // CREATE REFUND
    @Test
    void createRefund_shouldCreateRefundSuccessfully() {
        RefundCreateRequest request = RefundCreateRequest.builder()
                        .bookingId(100L)
                        .reason(RefundReason.BOOKING_CANCELLED)
                        .remarks("Customer cancelled booking")
                        .build();

        when(externalService.getBookingById(100L))
                .thenReturn(ApiResponse.success(booking));

        when(refundRepository.existsByBookingId(100L))
                .thenReturn(false);

        when(refundRepository.save(any(Refund.class)))
                .thenReturn(refund);

        RefundResponse response = refundService.createRefund(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getBookingId()).isEqualTo(100L);
        assertThat(response.getUserId()).isEqualTo(10L);
        assertThat(response.getPnr()).isEqualTo("ABC123");

        assertThat(response.getOriginalAmount())
                .isEqualByComparingTo("10000.00");

        assertThat(response.getCancellationFee())
                .isEqualByComparingTo("1000.00");

        assertThat(response.getRefundAmount())
                .isEqualByComparingTo("9000.00");

        assertThat(response.getStatus())
                .isEqualTo(RefundStatus.REQUESTED);

        verify(externalService)
                .getBookingById(100L);

        verify(refundRepository)
                .existsByBookingId(100L);

        verify(refundRepository)
                .save(any(Refund.class));
    }

    @Test
    void createRefund_shouldFailWhenBookingCannotBeRetrieved() {
        RefundCreateRequest request = RefundCreateRequest.builder()
                .bookingId(999L)
                .reason(RefundReason.BOOKING_CANCELLED)
                .build();

        when(externalService.getBookingById(999L))
                .thenReturn(null);

        assertThatThrownBy(() ->
                refundService.createRefund(request)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unable to retrieve booking");

        verify(refundRepository, never())
                .existsByBookingId(anyLong());

        verify(refundRepository, never())
                .save(any());
    }

    @Test
    void createRefund_shouldFailWhenBookingResponseHasNoData() {
        RefundCreateRequest request = RefundCreateRequest.builder()
                .bookingId(100L)
                .reason(RefundReason.BOOKING_CANCELLED)
                .build();

        when(externalService.getBookingById(100L))
                .thenReturn(ApiResponse.success(null));

        assertThatThrownBy(() ->
                refundService.createRefund(request)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unable to retrieve booking");

        verify(refundRepository, never())
                .save(any());
    }

    @Test
    void createRefund_shouldFailWhenRefundAlreadyExists() {
        RefundCreateRequest request =
                RefundCreateRequest.builder()
                        .bookingId(100L)
                        .reason(RefundReason.BOOKING_CANCELLED)
                        .build();

        when(externalService.getBookingById(100L))
                .thenReturn(ApiResponse.success(booking));

        when(refundRepository.existsByBookingId(100L))
                .thenReturn(true);

        assertThatThrownBy(() ->
                refundService.createRefund(request)
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Refund already exists");

        verify(refundRepository, never())
                .save(any());
    }

    // GET BY ID
    @Test
    void getById_shouldReturnRefund() {
        when(refundHelper.findById(1L))
                .thenReturn(refund);

        RefundResponse response = refundService.getById(1L);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getBookingId()).isEqualTo(100L);
        assertThat(response.getUserId()).isEqualTo(10L);
        assertThat(response.getPnr()).isEqualTo("ABC123");

        verify(refundHelper)
                .findById(1L);
    }

    @Test
    void getById_shouldThrowExceptionWhenRefundDoesNotExist() {
        when(refundHelper.findById(999L))
                .thenThrow(new IllegalArgumentException(
                        "Refund not found with ID: 999"
                ));

        assertThatThrownBy(() ->
                refundService.getById(999L)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Refund not found");

        verify(refundHelper)
                .findById(999L);
    }

    // GET BY BOOKING ID
    void getByBookingId_shouldReturnRefund() {
        when(refundHelper.findByBookingId(100L))
                .thenReturn(refund);

        RefundResponse response = refundService.getByBookingId(100L);

        assertThat(response).isNotNull();
        assertThat(response.getBookingId())
                .isEqualTo(100L);
        assertThat(response.getRefundAmount())
                .isEqualByComparingTo("9000.00");

        verify(refundHelper)
                .findByBookingId(100L);
    }

    @Test
    void getByBookingId_shouldThrowExceptionWhenRefundDoesNotExist() {
        when(refundHelper.findByBookingId(999L))
                .thenThrow(new IllegalArgumentException(
                        "Refund not found for booking ID: 999"
                ));

        assertThatThrownBy(() ->
                refundService.getByBookingId(999L)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Refund not found");

        verify(refundHelper)
                .findByBookingId(999L);
    }

    // GET BY USER ID
    @Test
    void getByUserId_shouldReturnPagedRefunds() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Refund> refundPage = new PageImpl<>(List.of(refund));
        when(refundRepository.findByUserId(10L, pageable))
                .thenReturn(refundPage);

        Page<RefundResponse> result = refundService.getByUserId(10L, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements())
                .isEqualTo(1);
        assertThat(result.getContent())
                .hasSize(1);
        assertThat(result.getContent().get(0).getBookingId())
                .isEqualTo(100L);

        verify(refundRepository)
                .findByUserId(10L, pageable);

    }

    // GET BY STATUS
    @Test
    void getByStatus_shouldReturnRefundsForValidStatus() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Refund> refundPage = new PageImpl<>(List.of(refund));

        when(refundRepository.findByStatus(RefundStatus.REQUESTED, pageable))
                .thenReturn(refundPage);

        Page<RefundResponse> result = refundService.getByStatus("requested", pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements())
                .isEqualTo(1);

        verify(refundRepository)
                .findByStatus(RefundStatus.REQUESTED, pageable);
    }

    @Test
    void getByStatus_shouldFailForInvalidStatus() {
        Pageable pageable = PageRequest.of(0, 10);

        assertThatThrownBy(() ->
                refundService.getByStatus("INVALID_STATUS", pageable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid refund status");

        verify(refundRepository, never())
                .findByStatus(any(), any());
    }

    // UPDATE STATUS
    @Test
    void updateStatus_shouldMoveRequestedToProcessing() {
        RefundStatusUpdateRequest request = RefundStatusUpdateRequest.builder()
                        .status(RefundStatus.PROCESSING)
                        .remarks("Refund processing started")
                        .build();

        refund.setStatus(RefundStatus.REQUESTED);

        when(refundHelper.findById(1L))
                .thenReturn(refund);
        when(refundRepository.save(refund))
                .thenReturn(refund);

        RefundResponse response = refundService.updateStatus(1L, request);

        assertThat(refund.getStatus())
                .isEqualTo(RefundStatus.PROCESSING);
        assertThat(refund.getRemarks())
                .isEqualTo("Refund processing started");
        assertThat(response)
                .isNotNull();

        verify(refundHelper)
                .findById(1L);
        verify(refundHelper)
                .validateStatusTransition(RefundStatus.REQUESTED, RefundStatus.PROCESSING);
        verify(refundRepository)
                .save(refund);
    }

    @Test
    void updateStatus_shouldRejectInvalidTransition() {
        RefundStatusUpdateRequest request = RefundStatusUpdateRequest.builder()
                        .status(RefundStatus.COMPLETED)
                        .build();

        refund.setStatus(RefundStatus.REQUESTED);

        when(refundHelper.findById(1L))
                .thenReturn(refund);

        doThrow(new IllegalStateException("Requested refund can only move to PROCESSING or REJECTED"))
                .when(refundHelper)
                .validateStatusTransition(RefundStatus.REQUESTED, RefundStatus.COMPLETED);

        assertThatThrownBy(() ->
                refundService.updateStatus(1L, request)
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("PROCESSING");

        verify(refundRepository, never())
                .save(any());
    }

    @Test
    void updateStatus_shouldUpdateGatewayRefundId() {
        RefundStatusUpdateRequest request = RefundStatusUpdateRequest.builder()
                        .status(RefundStatus.PROCESSING)
                        .gatewayRefundId("rfnd_123")
                        .remarks("Gateway processing")
                        .build();

        refund.setStatus(RefundStatus.REQUESTED);

        when(refundHelper.findById(1L))
                .thenReturn(refund);
        when(refundRepository.save(refund))
                .thenReturn(refund);

        RefundResponse response = refundService.updateStatus(1L, request);

        assertThat(refund.getStatus())
                .isEqualTo(RefundStatus.PROCESSING);
        assertThat(refund.getGatewayRefundId())
                .isEqualTo("rfnd_123");
        assertThat(refund.getRemarks())
                .isEqualTo("Gateway processing");

        verify(refundRepository)
                .save(refund);
    }

    // PROCESS REFUND





    // REJECT REFUND
    @Test
    void rejectRefund_shouldRejectRequestedRefund() {
        refund.setStatus(RefundStatus.REQUESTED);

        when(refundHelper.findById(1L))
                .thenReturn(refund);
        when(refundRepository.save(refund))
                .thenReturn(refund);

        RefundResponse response = refundService.rejectRefund(1L, "Refund rejected by admin");

        assertThat(response).isNotNull();
        assertThat(refund.getStatus())
                .isEqualTo(RefundStatus.REJECTED);
        assertThat(refund.getRemarks())
                .isEqualTo("Refund rejected by admin");

        verify(refundRepository)
                .save(refund);
    }

    @Test
    void rejectRefund_shouldFailWhenAlreadyRejected() {
        refund.setStatus(RefundStatus.REJECTED);

        when(refundHelper.findById(1L))
                .thenReturn(refund);

        assertThatThrownBy(() ->
                refundService.rejectRefund(1L, "Rejected again")
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Refund is already rejected");

        verify(refundRepository, never())
                .save(any());
    }

    @Test
    void rejectRefund_shouldFailWhenRefundIsCompleted() {
        refund.setStatus(RefundStatus.COMPLETED);

        when(refundHelper.findById(1L))
                .thenReturn(refund);

        assertThatThrownBy(() ->
                refundService.rejectRefund(1L, "Try to reject"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Completed refund cannot be rejected");

        verify(refundRepository, never())
                .save(any());
    }



}






















