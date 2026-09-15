package com.flightservice.elasticsearch.repository;

import com.flightservice.elasticsearch.document.FlightInstanceDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightInstanceSearchRepository extends ElasticsearchRepository<FlightInstanceDocument, Long> {


}
