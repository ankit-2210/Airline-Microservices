package com.airlineservice.controller;

import com.airlineportal.payload.request.Airlines.Aircraft.AircraftRequest;
import com.airlineportal.payload.response.Airlines.Aircraft.AircraftResponse;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineservice.service.AircraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/aircrafts")
public class AircraftController {
    private final AircraftService aircraftService;

    private static final int MAX_PAGE_SIZE = 100;

    private static final List<String> ALLOWED_SORT_FIELDS = List.of(
            "id",
            "code",
            "model",
            "manufacturer",
            "seatingCapacity",
            "rangeKm",
            "cruisingSpeedKmh",
            "maxAltitudeFt",
            "yearOfManufacture",
            "registrationDate",
            "nextMaintenanceDate",
            "aircraftStatus",
            "isAvailable",
            "createdAt",
            "updatedAt"
    );


    // Create Aircraft
    @PostMapping
    public ResponseEntity<ApiResponse<AircraftResponse>> createAircraft(@Valid @RequestBody AircraftRequest aircraftRequest, @RequestParam Long ownerId){
        AircraftResponse response = aircraftService.createAircraft(aircraftRequest, ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    // Get Aircraft By Id
    @GetMapping("/{id}")
    public ApiResponse<AircraftResponse> getById(@PathVariable Long id){
        return ApiResponse.success(aircraftService.getById(id));
    }

    // Get Aircraft By Owner
    @GetMapping("/owner/{ownerId}")
    public ApiResponse<Page<AircraftResponse>> getAircraftByOwner(@PathVariable Long ownerId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
                                                                                  @RequestParam(defaultValue = "code") String sortBy, @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = createPageable(page, size, sortBy, direction);
        return ApiResponse.success(aircraftService.allAircraftByOwner(ownerId, pageable));
    }

    // Get Aircraft By Airline
    @GetMapping("/airline/{airlineId}")
    public ApiResponse<Page<AircraftResponse>> getAircraftByAirline(@PathVariable Long airlineId,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "code") String sortBy, @RequestParam(defaultValue = "asc") String direction){
        Pageable pageable = createPageable(page, size, sortBy, direction);
        return ApiResponse.success(aircraftService.getAircraftByAirline(airlineId, pageable));
    }

    // Search Aircraft
    @GetMapping("/search")
    public ApiResponse<Page<AircraftResponse>> searchAircraft(@RequestParam String keyword,
                                                                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
                                                                    @RequestParam(defaultValue = "code") String sortBy, @RequestParam(defaultValue = "asc") String direction){
        Pageable pageable = createPageable(page, size, sortBy, direction);
        return ApiResponse.success(aircraftService.searchAircraft(keyword, pageable));
    }

    // Get Aircraft By Airport
    @GetMapping("/airport/{airportId}")
    public ApiResponse<Page<AircraftResponse>> getAircraftByAirport(@PathVariable Long airportId,
                                                                    @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
                                                                    @RequestParam(defaultValue = "code") String sortBy, @RequestParam(defaultValue = "asc") String direction){
        Pageable pageable = createPageable(page, size, sortBy, direction);
        return ApiResponse.success(aircraftService.getAircraftByAirport(airportId, pageable));
    }


    // Get Available Aircraft
    @GetMapping("/available")
    public ApiResponse<Page<AircraftResponse>> getAvailableAircraft(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "code") String sortBy, @RequestParam(defaultValue = "asc") String direction){
        Pageable pageable = createPageable(page, size, sortBy, direction);
        return ApiResponse.success(aircraftService.getAvailableAircraft(pageable));
    }

    // Update Aircraft
    @PutMapping("/{id}")
    public ApiResponse<AircraftResponse> updateAircraft(@PathVariable Long id, @RequestParam Long ownerId, @Valid @RequestBody AircraftRequest aircraftRequest) {
        return ApiResponse.success(aircraftService.updateAircraft(id, aircraftRequest, ownerId));
    }

    // Delete Aircraft
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAircraft(@PathVariable Long id, @RequestParam Long ownerId) {
        aircraftService.deleteAircraft(id, ownerId);
        return ApiResponse.success(null);
    }


    // Pageable Helper
    private Pageable createPageable(int page, int size, String sortBy, String direction){
        validatePagination(page, size);
        validateSortField(sortBy);

        Sort.Direction sortDirection;
        try{
            sortDirection = Sort.Direction.fromString(direction);
        }
        catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid sort direction: " + direction + ". Use 'asc' or 'desc'.");
        }

        int safeSize = Math.min(size, MAX_PAGE_SIZE);
        return PageRequest.of(page, safeSize, Sort.by(sortDirection, sortBy));
    }

    private void validatePagination(int page, int size){
        if(page < 0){
            throw new IllegalArgumentException("Page must be greater than or equal to 0");
        }

        if(size < 1){
            throw new IllegalArgumentException("Size must be greater than 0");
        }
    }

    private void validateSortField(String sortBy){
        if(sortBy == null || sortBy.isBlank()){
            throw new IllegalArgumentException("Sort field cannot be blank");
        }

        if(!ALLOWED_SORT_FIELDS.contains(sortBy)){
            throw new IllegalArgumentException("Invalid sort field: " + sortBy + ". Allowed fields: " + ALLOWED_SORT_FIELDS);
        }
    }

}
