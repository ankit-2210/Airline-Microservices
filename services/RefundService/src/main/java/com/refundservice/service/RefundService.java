package com.refundservice.service;

import com.airlineportal.payload.request.Refund.RefundCreateRequest;
import com.airlineportal.payload.request.Refund.RefundStatusUpdateRequest;
import com.airlineportal.payload.response.Refund.RefundResponse;
import org.springframework.data.domain.*;

public interface RefundService {
    RefundResponse createRefund(RefundCreateRequest request);
    RefundResponse getById(Long refundId);
    RefundResponse getByBookingId(Long bookingId);

    Page<RefundResponse> getByUserId(Long userId, Pageable pageable);
    Page<RefundResponse> getByStatus(String status, Pageable pageable);

    RefundResponse updateStatus(Long refundId, RefundStatusUpdateRequest request);
    RefundResponse processRefund(Long refundId);
    RefundResponse rejectRefund(Long refundId, String remarks);



}
