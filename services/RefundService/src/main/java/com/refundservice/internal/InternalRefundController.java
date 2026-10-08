package com.refundservice.internal;


import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Refund.RefundResponse;
import com.refundservice.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/refunds")
public class InternalRefundController {
    private final RefundService refundService;

    @GetMapping("/{refundId}")
    public ApiResponse<RefundResponse> getById(@PathVariable Long refundId){
        return ApiResponse.success(refundService.getById(refundId));
    }

    @GetMapping("/booking/{bookingId}")
    public ApiResponse<RefundResponse> getByBookingId(@PathVariable Long bookingId){
        return ApiResponse.success(refundService.getByBookingId(bookingId));
    }




}
