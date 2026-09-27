package com.airlineservice.internal;

import com.airlineportal.payload.response.Airlines.Aircraft.AircraftResponse;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineservice.service.AircraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/aircrafts")
public class InternalAircraftController {
    private final AircraftService aircraftService;

    @GetMapping("/{aircraftId}")
    public ApiResponse<AircraftResponse> getAircraftById(@PathVariable Long aircraftId) {
        return ApiResponse.success(aircraftService.getById(aircraftId));
    }

    @GetMapping("/{aircraftId}/airline/{airlineId}")
    public ApiResponse<Boolean> aircraftBelongsToAirline(@PathVariable Long aircraftId, @PathVariable Long airlineId){
        return ApiResponse.success(aircraftService.belongsToAirline(aircraftId, airlineId));
    }


}
