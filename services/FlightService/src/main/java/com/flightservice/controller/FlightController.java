package com.flightservice.controller;

import com.airlineportal.payload.request.Flight.FlightRequest;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.payload.response.Flight.FlightResponse;
import com.airlineportal.utils.Flight.FlightStatus;
import com.flightservice.service.FlightService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;

    // create flight
    @PostMapping("/airline/{airlineId}")
    public ApiResponse<FlightResponse> createFlight(@PathVariable Long airlineId, @Valid @RequestBody FlightRequest flightRequest) {
        return ApiResponse.success(flightService.createFlight(airlineId, flightRequest));
    }

    // get flight by id
    @GetMapping("/{flightId}")
    public ApiResponse<FlightResponse> getById(@PathVariable Long flightId){
        return ApiResponse.success(flightService.getById(flightId));
    }

    // Get all flights of an airline
    @GetMapping("/airline/{airlineId}")
    public ApiResponse<Page<FlightResponse>> getAllByAirline(@PathVariable Long airlineId, Pageable pageable){
        return ApiResponse.success(flightService.getAllByAirline(airlineId, pageable));
    }

    // Search flights by airline + route
    @GetMapping("/airline/{airlineId}/route")
    public ApiResponse<Page<FlightResponse>> searchByRoute(@PathVariable Long airlineId,
            @RequestParam Long departureAirportId, @RequestParam Long arrivalAirportId, Pageable pageable){
        return ApiResponse.success(flightService.searchByRoute(airlineId, departureAirportId, arrivalAirportId, pageable));
    }

    // Get flights by status
    @GetMapping("/status/{flightStatus}")
    public ApiResponse<Page<FlightResponse>> getByStatus(@PathVariable FlightStatus flightStatus, Pageable pageable){
        return ApiResponse.success(flightService.getByStatus(flightStatus, pageable));
    }

    // Get flights departing from airport
    @GetMapping("/departure/{departureAirportId}")
    public ApiResponse<Page<FlightResponse>> getByDepartureAirport(@PathVariable Long departureAirportId, Pageable pageable){
        return ApiResponse.success(flightService.getByDepartureAirport(departureAirportId, pageable));
    }

    // Get flights arriving at airport
    @GetMapping("/arrival/{arrivalAirportId}")
    public ApiResponse<Page<FlightResponse>> getByArrivalAirport(@PathVariable Long arrivalAirportId, Pageable pageable){
        return ApiResponse.success(flightService.getByArrivalAirport(arrivalAirportId, pageable));
    }

    // Get flights between two airports
    @GetMapping("/route")
    public ApiResponse<Page<FlightResponse>> getByRoute(@RequestParam Long departureAirportId, @RequestParam Long arrivalAirportId, Pageable pageable) {
        return ApiResponse.success(flightService.getByRoute(departureAirportId, arrivalAirportId, pageable));
    }

    // Upcoming flights of an airline
    @GetMapping("/airline/{airlineId}/upcoming")
    public ApiResponse<Page<FlightResponse>> getUpcomingFlights(@PathVariable Long airlineId, Pageable pageable){
        return ApiResponse.success(flightService.getUpcomingFlights(airlineId, pageable));
    }

    // Search flights by route + departure date
    @GetMapping("/search")
    public ApiResponse<Page<FlightResponse>> searchFlightsByDate(@RequestParam Long departureAirportId, @RequestParam Long arrivalAirportId,
            @RequestParam LocalDate departureDate, Pageable pageable){
        return ApiResponse.success(flightService.searchFlightsByDate(departureAirportId, arrivalAirportId, departureDate, pageable));
    }

    // Airline updates flight
    @PutMapping("/{flightId}/airline/{airlineId}")
    public ApiResponse<FlightResponse> updateFlight(@PathVariable Long flightId, @PathVariable Long airlineId, @Valid @RequestBody FlightRequest request){
        return ApiResponse.success(flightService.updateFlight(flightId, request, airlineId));
    }

    // Airline deletes flight
    @DeleteMapping("/{flightId}/airline/{airlineId}")
    public void deleteFlight(@PathVariable Long flightId, @PathVariable Long airlineId){
        flightService.deleteFlight(flightId, airlineId);
    }

    // Airline changes flight status
    @PatchMapping("/{flightId}/airline/{airlineId}/status")
    public ApiResponse<FlightResponse> changeStatus(@PathVariable Long flightId, @PathVariable Long airlineId, @RequestParam FlightStatus flightStatus){
        return ApiResponse.success(flightService.changeStatus(flightId, flightStatus, airlineId));
    }



}
