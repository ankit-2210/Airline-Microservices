package com.promptservice.tools;

import com.airlineportal.client.LocationFeignClient;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Location.Airport.AirportResponse;
import com.airlineportal.payload.response.Location.City.CityResponse;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationAITools {
    private final LocationFeignClient locationFeignClient;

    @Tool("Get city details using the internal city ID.")
    public ApiResponse<CityResponse> getCityById(Long cityId) {
        return locationFeignClient.getCityById(cityId);
    }

    @Tool("Get city details using its city code.")
    public ApiResponse<CityResponse> getCityByCode(String cityCode) {
        return locationFeignClient.getCityByCode(cityCode);
    }

    @Tool("Get airport details using the internal airport ID.")
    public ApiResponse<AirportResponse> getAirportById(Long airportId) {
        return locationFeignClient.getAirportById(airportId);
    }

    @Tool("Get airport details using its IATA airport code, such as CCU or DEL.")
    public ApiResponse<AirportResponse> getAirportByIataCode(String iataCode) {
        return locationFeignClient.getAirportByIataCode(iataCode);
    }



}
