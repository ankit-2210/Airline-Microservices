package com.flightservice.elasticsearch.sync;

import com.flightservice.elasticsearch.service.FlightSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightSearchSyncService {
    private final FlightSearchService flightSearchService;

    public void onFlightCreated(Long flightId){
        flightSearchService.indexFlight(flightId);
    }

    public void onFlightUpdated(Long flightId){
        flightSearchService.reindexFlight(flightId);
    }

    public void onFlightDeleted(Long flightId){
        flightSearchService.deleteFlight(flightId);
    }
}
