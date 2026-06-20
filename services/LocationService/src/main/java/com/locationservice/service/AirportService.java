package com.locationservice.service;

import com.microservices.payload.request.Location.Airport.AirportRequest;
import com.microservices.payload.response.Location.Airport.AirportResponse;
import org.springframework.data.domain.*;
import java.util.*;

public interface AirportService {
    AirportResponse createAirport(AirportRequest airportRequest);
    AirportResponse getAirportById(Long id);
    AirportResponse getAirportByIataCode(String iataCode);

    Page<AirportResponse> getAllAirports(Pageable pageable);
    Page<AirportResponse> searchAirports(String keyword, Pageable pageable);
    Page<AirportResponse> getAirportByCityId(Long cityId, Pageable pageable);
    Page<AirportResponse> getAirportByCountryCode(String countryCode, Pageable pageable);

    AirportResponse updateAirport(Long id, AirportRequest airportRequest);
    AirportResponse changeStatus(Long id, Boolean active);

    List<AirportResponse> getAirportDropdown();

    void deleteAirport(Long id);


}
