package com.refundservice.service.Impl;

import com.airlineportal.payload.request.Payment.PaymentRefundRequest;
import com.airlineportal.payload.request.Refund.RefundCreateRequest;
import com.airlineportal.payload.request.Refund.RefundStatusUpdateRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.airlineportal.payload.response.Payment.PaymentRefundResponse;
import com.airlineportal.payload.response.Refund.RefundResponse;
import com.airlineportal.utils.Refund.RefundStatus;
import com.refundservice.external.ExternalService;
import com.refundservice.helper.RefundHelper;
import com.refundservice.mapper.RefundMapper;
import com.refundservice.model.Refund;
import com.refundservice.repository.RefundRepository;
import com.refundservice.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefundServiceImpl implements RefundService {
    private final RefundRepository refundRepository;
    private final RefundHelper refundHelper;
    private final ExternalService externalService;


    @Override
    @Transactional
    public RefundResponse createRefund(RefundCreateRequest request) {

        // 3. Get booking
        ApiResponse<BookingResponse> bookingResponse = externalService.getBookingById(request.getBookingId());
        if(bookingResponse == null || bookingResponse.getData() == null){
            throw new IllegalArgumentException("Unable to retrieve booking");
        }
        BookingResponse booking = bookingResponse.getData();

        refundHelper.validateBookingForRefund(booking);

        if(refundRepository.existsByBookingId(request.getBookingId())){
            throw new IllegalStateException("Refund already exists for booking ID: " + request.getBookingId());
        }

        BigDecimal originalAmount = booking.getTotalAmount();
        BigDecimal cancellationFee = refundHelper.calculateCancellationFee(originalAmount);
        BigDecimal refundAmount = refundHelper.calculateRefundAmount(originalAmount, cancellationFee);

        Refund refund = RefundMapper.toEntity(request, booking, originalAmount, cancellationFee, refundAmount);
        Refund saved = refundRepository.save(refund);
        return RefundMapper.toResponse(saved);

    }

    @Override
    public RefundResponse getById(Long refundId) {
        Refund refund = refundHelper.findById(refundId);
        return RefundMapper.toResponse(refund);
    }

    @Override
    public RefundResponse getByBookingId(Long bookingId) {
        Refund refund = refundHelper.findByBookingId(bookingId);
        return RefundMapper.toResponse(refund);
    }

    @Override
    public Page<RefundResponse> getByUserId(Long userId, Pageable pageable) {
        return refundRepository.findByUserId(userId, pageable)
                .map(RefundMapper::toResponse);
    }

    @Override
    public Page<RefundResponse> getByStatus(String status, Pageable pageable) {
        RefundStatus refundStatus;
        try {
            refundStatus = RefundStatus.valueOf(status.toUpperCase());
        }
        catch(IllegalArgumentException e){
            throw new IllegalArgumentException("Invalid refund status: " + status);
        }

        return refundRepository.findByStatus(refundStatus, pageable)
                .map(RefundMapper::toResponse);

    }

    @Override
    @Transactional
    public RefundResponse updateStatus(Long refundId, RefundStatusUpdateRequest request) {
        Refund refund = refundHelper.findById(refundId);

        refundHelper.validateStatusTransition(refund.getStatus(), request.getStatus());

        refund.setStatus(request.getStatus());
        if(request.getGatewayRefundId() != null && !request.getGatewayRefundId().isBlank()){
            refund.setGatewayRefundId(request.getGatewayRefundId());
        }
        if(request.getRemarks() != null && !request.getRemarks().isBlank()){
            refund.setRemarks(request.getRemarks());
        }
        refund.setUpdatedAt(LocalDateTime.now());

        Refund saved = refundRepository.save(refund);
        return RefundMapper.toResponse(saved);

    }

    @Override
    @Transactional
    public RefundResponse processRefund(Long refundId) {
        Refund refund = refundHelper.findById(refundId);

        refundHelper.validateCanProcessRefund(refund);

        // Move refund to PROCESSING before calling PaymentService.
        refund.setStatus(RefundStatus.PROCESSING);
        refund.setUpdatedAt(LocalDateTime.now());
        refundRepository.save(refund);

        PaymentRefundRequest paymentRequest = PaymentRefundRequest.builder()
                .bookingId(refund.getBookingId())
                .amount(refund.getRefundAmount())
                .build();

        // Call PaymentService. PaymentService is responsible for communicating with Razorpay.
        ApiResponse<PaymentRefundResponse> paymentResponse = externalService.refundPayment(paymentRequest);
        if(paymentResponse == null || paymentResponse.getData() == null){
            throw new IllegalArgumentException("Unable to retrieve payment refund");
        }
        PaymentRefundResponse paymentRefundResponse = paymentResponse.getData();
        // PaymentService failed to process refund
        if(paymentRefundResponse.getGatewayRefundId() == null || paymentRefundResponse.getGatewayRefundId().isBlank()){
            refund.setStatus(RefundStatus.FAILED);
            refund.setRemarks("Payment gateway refund failed");
            refund.setUpdatedAt(LocalDateTime.now());

            Refund failedRefund = refundRepository.save(refund);
            return RefundMapper.toResponse(failedRefund);
        }

        // Razorpay refund succeeded.
        refund.setGatewayRefundId(paymentRefundResponse.getGatewayRefundId());
        refund.setStatus(RefundStatus.COMPLETED);
        refund.setUpdatedAt(LocalDateTime.now());

        Refund completedRefund = refundRepository.save(refund);
        return RefundMapper.toResponse(completedRefund);


    }

    @Override
    @Transactional
    public RefundResponse rejectRefund(Long refundId, String remarks) {
        Refund refund = refundHelper.findById(refundId);
        if(refund.getStatus() == RefundStatus.COMPLETED){
            throw new IllegalStateException("Completed refund cannot be rejected");
        }
        if(refund.getStatus() == RefundStatus.REJECTED){
            throw new IllegalStateException("Refund is already rejected");
        }

        refund.setStatus(RefundStatus.REJECTED);
        refund.setRemarks(remarks);
        refund.setUpdatedAt(LocalDateTime.now());

        Refund saved = refundRepository.save(refund);
        return RefundMapper.toResponse(saved);
    }
}
