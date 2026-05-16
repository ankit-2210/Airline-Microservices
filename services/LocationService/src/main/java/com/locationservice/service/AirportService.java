package com.locationservice.service;

import com.microservices.payload.request.Airport.AirportRequest;
import com.microservices.payload.response.Airport.AirportResponse;
import java.util.*;

public interface AirportService {
    AirportResponse createAirport(AirportRequest airportRequest);
    AirportResponse getAirportById(Long id);

    List<AirportResponse> getAllAirports();

    AirportResponse updateAirport(Long id, AirportRequest airportRequest);
    void deleteAirport(Long id);
    List<AirportResponse> getAirportByCityId(Long cityId);

}
