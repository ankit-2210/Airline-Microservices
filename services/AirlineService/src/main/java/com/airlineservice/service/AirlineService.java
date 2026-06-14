package com.airlineservice.service;

import com.microservices.payload.request.Airlines.Airline.AirlineRequest;
import com.microservices.payload.response.Airline.AirlineDropdownItem;
import com.microservices.payload.response.Airline.AirlineResponse;
import com.microservices.utils.Airline.AirlineStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.*;

public interface AirlineService {
    AirlineResponse createAirline(AirlineRequest airlineRequest, Long ownerId);
    AirlineResponse getAirlineByOwner(Long ownerId);
    AirlineResponse getAirlineById(Long id);

    Page<AirlineResponse> getAllAirlines(Pageable pageable);
    AirlineResponse updateAirline(Long airlineId, AirlineRequest airlineRequest, Long ownerId);
    void deleteAirline(Long id, Long ownerId);

    AirlineResponse changeStatusByAdmin(Long airlineId, AirlineStatus airlineStatus);
    List<AirlineDropdownItem> getAirlineDropdown();


}
