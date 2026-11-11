package com.promptservice.tools;

import com.airlineportal.client.SeatFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Seat.SeatResponse;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatAITools {
    private final SeatFeignClient seatFeignClient;

    @Tool("""
        Retrieve the status and details of a particular seat on a flight instance.
        Use this when the user asks about a specific seat number and flight instance.
        """)
    public ApiResponse<SeatResponse> getSeat(Long flightInstanceId, String seatNumber) {
        return seatFeignClient.getSeat(flightInstanceId, seatNumber);
    }


}
