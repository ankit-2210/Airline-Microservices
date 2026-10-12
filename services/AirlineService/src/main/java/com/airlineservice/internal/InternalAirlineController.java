package com.airlineservice.internal;

import com.airlineportal.payload.response.Airlines.Airline.AirlineResponse;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.utils.Airline.AirlineStatus;
import com.airlineservice.service.AirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/airlines")
public class InternalAirlineController {
    private final AirlineService airlineService;

    @GetMapping("/{airlineId}")
    public ApiResponse<AirlineResponse> getAirlineById(@PathVariable Long airlineId) {
        return ApiResponse.success(airlineService.getAirlineById(airlineId));
    }

    @PatchMapping("/{airlineId}/status")
    public ApiResponse<AirlineResponse> changeStatus(@PathVariable Long airlineId, @RequestParam AirlineStatus airlineStatus) {
        return ApiResponse.success(airlineService.changeStatusByAdmin(airlineId, airlineStatus));
    }



}
