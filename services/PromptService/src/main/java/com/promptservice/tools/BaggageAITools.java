package com.promptservice.tools;

import com.airlineportal.client.BaggageFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Baggage.BaggageResponse;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BaggageAITools {
    private final BaggageFeignClient baggageFeignClient;

    @Tool("""
        Retrieve baggage details using the baggage ID.
        Use this when the user asks about a specific baggage record.
        The backend must verify that the record belongs to the authenticated user.
        """)
    public ApiResponse<BaggageResponse> getBaggageById(Long baggageId) {
        return baggageFeignClient.getById(baggageId);
    }


    @Tool("""
        Retrieve all baggage records associated with a booking.
        Use this when the user asks about baggage associated with a reservation.
        The backend must verify that the booking belongs to the authenticated user.
        """)
    public ApiResponse<List<BaggageResponse>> getBaggageByBookingId(Long bookingId) {
        return baggageFeignClient.getByBookingId(bookingId);
    }


    @Tool("""
        Retrieve baggage records associated with a passenger.
        Use this when the user asks about a passenger's baggage.
        The backend must verify that the passenger belongs to the authenticated user.
        """)
    public ApiResponse<List<BaggageResponse>> getBaggageByPassengerId(Long passengerId) {
        return baggageFeignClient.getByPassengerId(passengerId);
    }



}
