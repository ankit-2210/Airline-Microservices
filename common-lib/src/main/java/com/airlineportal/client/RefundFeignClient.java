package com.airlineportal.client;


import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Refund.RefundResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "REFUNDSERVICE",
        path = "/internal/refunds"
)
public interface RefundFeignClient {

    @GetMapping("/{refundId}")
    ApiResponse<RefundResponse> getById(@PathVariable Long refundId);

    @GetMapping("/booking/{bookingId}")
    ApiResponse<RefundResponse> getByBookingId(@PathVariable Long bookingId);



}
