package com.locationservice.service;

import com.microservices.payload.request.Airport.AirportRequest;
import com.microservices.payload.response.Airport.AirportResponse;
import org.springframework.data.domain.*;

import java.util.*;

public interface AirportService {
    AirportResponse createAirport(AirportRequest airportRequest);
    AirportResponse getAirportById(Long id);
    AirportResponse getAirportByIataCode(String iataCode);

    Page<AirportResponse> getAllAirports(Pageable pageable);
    Page<AirportResponse> searchAirports(String keyword, Pageable pageable);
    Page<AirportResponse> getAirportByCityId(Long cityId, Pageable pageable);

    AirportResponse updateAirport(Long id, AirportRequest airportRequest);

    void deleteAirport(Long id);


}
