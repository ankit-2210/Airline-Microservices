package com.promptservice.tools;

import com.airlineportal.client.RefundFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Refund.RefundResponse;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefundAITools {
    private final RefundFeignClient refundFeignClient;

    @Tool("""
        Retrieve refund details using the refund ID.
        Use this when the user asks about a specific refund.
        Ensure the refund belongs to the currently authenticated user's booking.
        """)
    public ApiResponse<RefundResponse> getRefundById(Long refundId) {
        return refundFeignClient.getById(refundId);
    }


    @Tool("""
        Retrieve refund information for a particular booking.
        Use this when the user asks about the refund associated with a booking.
        The booking must belong to the currently authenticated user.
        """)
    public ApiResponse<RefundResponse> getRefundByBookingId(Long bookingId) {
        return refundFeignClient.getByBookingId(bookingId);
    }


    @Tool("""
        Retrieve refunds filtered by their status.
        This operation is intended for authorized administrative users only.
        Do not use it to retrieve general users' refund information.
        """)
    public ApiResponse<Page<RefundResponse>> getRefundsByStatus(String status, Integer page, Integer size) {
        int pageNumber = page == null ? 0 : Math.max(page, 0);
        int pageSize = size == null ? 10 : Math.min(Math.max(size, 1), 50);

        return refundFeignClient.getByStatus(status, PageRequest.of(pageNumber, pageSize));
    }

}
