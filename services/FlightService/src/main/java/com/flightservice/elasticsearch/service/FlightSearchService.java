package com.flightservice.elasticsearch.service;

import com.flightservice.elasticsearch.document.FlightDocument;
import com.flightservice.elasticsearch.document.FlightInstanceDocument;
import org.springframework.data.domain.*;
import org.springframework.data.util.Streamable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FlightSearchService {

    FlightDocument indexFlight(Long flightId);
    FlightDocument save(FlightDocument document);

    void deleteFlight(Long flightId);
    void reindexFlight(Long flightId);
    void reindexAll();

    Optional<FlightDocument> getById(Long flightId);

    Streamable<FlightDocument> search(Long airlineId, Long departureAirportId, Long arrivalAirportId,
                                      String keyword,
                                      LocalDateTime departureFrom, LocalDateTime departureTo, Pageable pageable);



}
