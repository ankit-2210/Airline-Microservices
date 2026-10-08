package com.refundservice.controller;


import com.airlineportal.payload.request.Refund.RefundCreateRequest;
import com.airlineportal.payload.request.Refund.RefundStatusUpdateRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Refund.RefundResponse;
import com.refundservice.service.RefundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refunds")
public class RefundController {
    private final RefundService refundService;

    @PostMapping
    public ApiResponse<RefundResponse> createRefund(@Valid @RequestBody RefundCreateRequest request) {
        return ApiResponse.success(refundService.createRefund(request));
    }

    @GetMapping("/{refundId}")
    public ApiResponse<RefundResponse> getById(@PathVariable Long refundId){
        return ApiResponse.success(refundService.getById(refundId));
    }

    @GetMapping("/booking/{bookingId}")
    public ApiResponse<RefundResponse> getByBookingId(@PathVariable Long bookingId){
        return ApiResponse.success(refundService.getByBookingId(bookingId));
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<Page<RefundResponse>> getByUserId(@PathVariable Long userId, @PageableDefault(size = 10) Pageable pageable){
        return ApiResponse.success(refundService.getByUserId(userId, pageable));
    }

    @GetMapping("/status/{status}")
    public ApiResponse<Page<RefundResponse>> getByStatus(@PathVariable String status, @PageableDefault(size = 10) Pageable pageable){
        return ApiResponse.success(refundService.getByStatus(status, pageable));
    }

    @PostMapping("/{refundId}/process")
    public ApiResponse<RefundResponse> processRefund(@PathVariable Long refundId){
        return ApiResponse.success(refundService.processRefund(refundId));
    }

    @PutMapping("/{refundId}/status")
    public ApiResponse<RefundResponse> updateStatus(@PathVariable Long refundId, @Valid @RequestBody RefundStatusUpdateRequest request){
        return ApiResponse.success(refundService.updateStatus(refundId, request));

    }
    @PutMapping("/{refundId}/reject")
    public ApiResponse<RefundResponse> rejectRefund(@PathVariable Long refundId, @RequestParam(required = false) String remarks){
        return ApiResponse.success(refundService.rejectRefund(refundId, remarks));
    }



}
