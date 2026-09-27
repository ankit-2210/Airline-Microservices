package com.airlineportal.client;

import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Location.Airport.AirportResponse;
import com.airlineportal.payload.response.Location.City.CityResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "LOCATIONSERVICE",
        path = "/internal"
)
public interface LocationFeignClient {

    @GetMapping("/cities/{cityId}")
    ApiResponse<CityResponse> getCityById(@PathVariable Long cityId);

    @GetMapping("/cities/code/{cityCode}")
    ApiResponse<CityResponse> getCityByCode(@PathVariable String cityCode);

    @GetMapping("/airports/{airportId}")
    ApiResponse<AirportResponse> getAirportById(@PathVariable Long airportId);

    @GetMapping("/airports/iata/{iataCode}")
    ApiResponse<AirportResponse> getAirportByIataCode(@PathVariable String iataCode);


}

