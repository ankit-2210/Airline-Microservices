package com.flightservice.elasticsearch.repository;

import com.flightservice.elasticsearch.document.FlightDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface FlightSearchRepository extends ElasticsearchRepository<FlightDocument, Long> {
    List<FlightDocument> findByFlightNumber(String flightNumber);

    List<FlightDocument> findByAirlineId(Long airlineId);
    List<FlightDocument> findByDepartureAirportId(Long departureAirportId);
    List<FlightDocument> findByArrivalAirportId(Long arrivalAirportId);

    List<FlightDocument> findByDepartureAirportIdAndArrivalAirportId(Long departureAirportId, Long arrivalAirportId);
    List<FlightDocument> findByFlightStatus(String flightStatus);

    List<FlightDocument> findByActiveTrue();


}
