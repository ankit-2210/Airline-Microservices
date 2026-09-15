package com.flightservice.elasticsearch.service.Impl;

import com.flightservice.elasticsearch.document.FlightDocument;
import com.flightservice.elasticsearch.document.FlightInstanceDocument;
import com.flightservice.elasticsearch.mapper.FlightDocumentMapper;
import com.flightservice.elasticsearch.repository.FlightSearchRepository;
import com.flightservice.elasticsearch.service.FlightSearchService;
import com.flightservice.model.Flight;
import com.flightservice.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FlightSearchServiceImpl implements FlightSearchService {
    private final FlightSearchRepository flightSearchRepository;
    private final FlightRepository flightRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    @Transactional(readOnly = true)
    public FlightDocument indexFlight(Long flightId) {
        if(flightId == null){
            throw new IllegalArgumentException( "Flight id cannot be null" );
        }

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found with id: " + flightId));

        FlightDocument document = FlightDocumentMapper.toDocument(flight);
        return flightSearchRepository.save(document);
    }

    @Override
    public FlightDocument save(FlightDocument document) {
        if(document == null){
            throw new IllegalArgumentException("Flight document cannot be null");
        }

        return flightSearchRepository.save(document);
    }

    @Override
    public void deleteFlight(Long flightId) {
        if(flightId == null){
            throw new IllegalArgumentException("Flight id cannot be null");
        }

        flightSearchRepository.deleteById(flightId);
    }

    @Override
    @Transactional(readOnly = true)
    public void reindexFlight(Long flightId) {
        indexFlight(flightId);
    }

    @Override
    @Transactional(readOnly = true)
    public void reindexAll(){
        Iterable<Flight> flights = flightRepository.findAll();
        for(Flight flight : flights){
            FlightDocument document = FlightDocumentMapper.toDocument(flight);
            flightSearchRepository.save(document);
        }
    }

    @Override
    public Optional<FlightDocument> getById(Long flightId) {
        if(flightId == null)
            return Optional.empty();

        return flightSearchRepository.findById(flightId);
    }

    @Override
    public Streamable<FlightDocument> search(Long airlineId, Long departureAirportId, Long arrivalAirportId, String keyword, LocalDateTime departureFrom, LocalDateTime departureTo, Pageable pageable) {
        Criteria criteria = new Criteria();

        boolean hasCriteria = false;
        if(keyword != null && !keyword.isBlank()){
            String normalizedKeyword = keyword.trim().toUpperCase();
            criteria = criteria.or(new Criteria("flightNumber").matches(normalizedKeyword));
            hasCriteria = true;
        }

        if(airlineId != null){
            Criteria airlineCriteria = new Criteria("airlineId").is(airlineId);
            criteria = hasCriteria ? criteria.and(airlineCriteria) : airlineCriteria;
            hasCriteria = true;
        }

        if(departureAirportId != null){
            Criteria departureCriteria = new Criteria("departureAirportId").is(departureAirportId);
            criteria = hasCriteria ? criteria.and(departureCriteria) : departureCriteria;
            hasCriteria = true;
        }

        if(arrivalAirportId != null){
            Criteria arrivalCriteria = new Criteria("arrivalAirportId").is(arrivalAirportId);
            criteria = hasCriteria ? criteria.and(arrivalCriteria) : arrivalCriteria;
            hasCriteria = true;
        }

        if(departureFrom != null){
            Criteria departureFromCriteria = new Criteria("departureFrom").is(departureFrom);
            criteria = hasCriteria ? criteria.and(departureFromCriteria) : departureFromCriteria;
            hasCriteria = true;
        }
        if(departureTo != null){
            Criteria departureToCriteria = new Criteria("departureTo").is(departureTo);
            criteria = hasCriteria ? criteria.and(departureToCriteria) : departureToCriteria;
            hasCriteria = true;
        }

        Criteria activeCriteria = new Criteria("active").is(true);
        criteria = hasCriteria ? criteria.and(activeCriteria) : activeCriteria;

        CriteriaQuery query = new CriteriaQuery(criteria);
        query.setPageable(pageable);

        return elasticsearchOperations.search(query, FlightDocument.class)
                .map(hit -> hit.getContent());

    }
}
