package com.flightservice.elasticsearch.controller;

import com.airlineportal.payload.response.ApiResponse;
import com.flightservice.elasticsearch.document.FlightDocument;
import com.flightservice.elasticsearch.service.FlightSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/flights/search")
@RequiredArgsConstructor
public class FlightSearchController {
    private final FlightSearchService flightSearchService;

    @GetMapping
    public ApiResponse<Page<FlightDocument>> search(@RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long airlineId,
            @RequestParam(required = false) Long departureAirportId, @RequestParam(required = false) Long arrivalAirportId,
            @RequestParam(required = false) LocalDateTime departureFrom, @RequestParam(required = false) LocalDateTime departureTo,
            @PageableDefault(size = 20) Pageable pageable){

        return ApiResponse.success(
                (Page<FlightDocument>) flightSearchService.search(
                        airlineId,
                        departureAirportId, arrivalAirportId,
                        keyword,
                        departureFrom, departureTo,
                        pageable));

    }

    @GetMapping("/{flightId}")
    public ApiResponse<Optional<FlightDocument>> getById(@PathVariable Long flightId){
        return ApiResponse.success(flightSearchService.getById(flightId));
    }

    @PostMapping("/reindex")
    public ApiResponse<String> reindexAll(){
        flightSearchService.reindexAll();
        return ApiResponse.success("All flights reindex successfully");
    }

    @DeleteMapping("/{flightId}")
    public void deleteFromIndex(@PathVariable Long flightId){
        flightSearchService.deleteFlight(flightId);

    }




}
