package com.locationservice.service;

import com.airlineportal.payload.request.Location.Airport.AirportRequest;
import com.airlineportal.payload.response.Location.Airport.AirportResponse;
import org.springframework.data.domain.*;
import java.util.*;

public interface AirportService {
    AirportResponse createAirport(AirportRequest airportRequest);
    AirportResponse getAirportById(Long id);
    AirportResponse getAirportByIataCode(String iataCode);

    AirportResponse updateAirport(Long id, AirportRequest airportRequest);

    void deleteAirport(Long id);

    Page<AirportResponse> getAllAirports(Pageable pageable);
    Page<AirportResponse> searchAirports(String keyword, Pageable pageable);

    List<AirportResponse> getAirportsByCity(Long cityId);

    Page<AirportResponse> getAirportsByCity(Long cityId, Pageable pageable);
    Page<AirportResponse> getAirportsByCountry(String countryCode, Pageable pageable);

    List<AirportResponse> getAirportDropdown();

    boolean airportExists(String iataCode);


}
