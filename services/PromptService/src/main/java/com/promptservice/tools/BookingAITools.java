package com.promptservice.tools;

import com.airlineportal.client.BookingFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Booking.BookingResponse;
import com.promptservice.service.AIContextService;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingAITools {
    private final BookingFeignClient bookingFeignClient;
    private final AIContextService aiContextService;

    @Tool("""
        Retrieve the bookings of the currently authenticated user.
        Use this when the user asks about their bookings, booking history,
        upcoming trips, or previous reservations.
        Never ask the user for their user ID. The authenticated application
        context determines which user's bookings can be retrieved.
        """)
    public ApiResponse<Page<BookingResponse>> getMyBookings(Integer page, Integer size) {
        Long userId = aiContextService.getCurrentUserId();

        int pageNumber = page == null ? 0 : Math.max(page, 0);
        int pageSize = size == null ? 10 : Math.min(Math.max(size, 1), 50);

        return bookingFeignClient.getBookingsByUser(userId, PageRequest.of(pageNumber, pageSize));
    }

}
