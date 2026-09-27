package com.locationservice.internal;


import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Location.City.CityResponse;
import com.locationservice.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/cities")
public class InternalCityController {
    private final CityService cityService;

    @GetMapping("/{cityId}")
    public ApiResponse<CityResponse> getCityById(@PathVariable Long cityId){
        return ApiResponse.success(cityService.getCityById(cityId));
    }

    @GetMapping("/code/{cityCode}")
    public ApiResponse<CityResponse> getCityByCode(@PathVariable String cityCode){
        return ApiResponse.success(cityService.getCityByCode(cityCode));
    }

}
