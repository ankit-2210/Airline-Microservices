package com.airlineservice.service;


import com.airlineportal.payload.request.Airlines.Aircraft.AircraftRequest;
import com.airlineportal.payload.response.Airlines.Aircraft.AircraftResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AircraftService {
    AircraftResponse createAircraft(AircraftRequest aircraftRequest, Long ownerId);
    AircraftResponse getById(Long id);

    Page<AircraftResponse> allAircraftByOwner(Long ownerId, Pageable pageable);
    Page<AircraftResponse> getAircraftByAirline(Long airlineId, Pageable pageable);
    Page<AircraftResponse> searchAircraft(String keyword, Pageable pageable);

    Page<AircraftResponse> getAircraftByAirport(Long airportId, Pageable pageable);
    Page<AircraftResponse> getAvailableAircraft(Pageable pageable);

    AircraftResponse updateAircraft(Long id, AircraftRequest aircraftRequest, Long ownerId);

    void deleteAircraft(Long id, Long ownerId);


}
