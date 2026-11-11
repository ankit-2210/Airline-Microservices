package com.promptservice.tools;

import com.airlineportal.client.FlightFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Flight.FlightInstanceResponse;
import com.airlineportal.payload.response.Flight.FlightResponse;
import com.airlineportal.payload.response.Flight.FlightScheduleResponse;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlightAITools {
    private final FlightFeignClient flightFeignClient;

    @Tool("""
        Retrieve flight details using the flight ID.
        Use this when the user asks about a specific flight and its ID is known.
        """)
    public ApiResponse<FlightResponse> getFlightById(Long flightId) {
        return flightFeignClient.getFlightById(flightId);
    }

    @Tool("""
        Retrieve a flight belonging to a particular airline.
        Use this when both the flight ID and airline ID are known.
        """)
    public ApiResponse<FlightResponse> getFlightByAirline(Long flightId, Long airlineId) {
        return flightFeignClient.getFlightByAirline(flightId, airlineId);
    }

    @Tool("""
        Retrieve a flight instance using its instance ID.
        Use this to get details about a particular scheduled flight instance.
        """)
    public ApiResponse<FlightInstanceResponse> getFlightInstanceById(Long instanceId) {
        return flightFeignClient.getInstanceById(instanceId);
    }

    @Tool("""
        Retrieve a flight schedule using its schedule ID.
        Use this when the user asks for details about a known flight schedule.
        """)
    public ApiResponse<FlightScheduleResponse> getFlightScheduleById(Long scheduleId) {
        return flightFeignClient.getScheduleById(scheduleId);
    }

}
