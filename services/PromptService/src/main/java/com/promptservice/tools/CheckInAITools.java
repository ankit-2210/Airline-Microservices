package com.promptservice.tools;

import com.airlineportal.client.CheckInFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.CheckIn.CheckInResponse;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckInAITools {
    private final CheckInFeignClient checkInFeignClient;

    @Tool("""
        Retrieve check-in details using the check-in ID.
        Use this when the user provides a check-in reference.
        The backend must verify that the check-in belongs to the authenticated user.
        """)
    public ApiResponse<CheckInResponse> getCheckInById(Long checkInId) {
        return checkInFeignClient.getCheckInById(checkInId);
    }

    @Tool("""
        Retrieve check-in information for a passenger on a particular booking.
        Use this when the booking ID and passenger ID are known.
        The backend must verify ownership of the booking before returning data.
        """)
    public ApiResponse<CheckInResponse> getCheckInByBookingAndPassenger(Long bookingId, Long passengerId) {
        return checkInFeignClient.getByBookingAndPassenger(bookingId, passengerId);
    }



}
