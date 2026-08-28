package com.airlineservice.controller;

import com.airlineportal.payload.request.Airlines.Airline.AirlineRequest;
import com.airlineportal.payload.response.Airlines.Airline.AirlineDropdownItem;
import com.airlineportal.payload.response.Airlines.Airline.AirlineResponse;
import com.airlineportal.payload.response.ApiResponse;
import com.airlineportal.utils.Airline.AirlineStatus;
import com.airlineservice.service.AirlineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/airlines")
public class AirlineController {
    private final AirlineService airlineService;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 100;

    private static final List<String> ALLOWED_SORT_FIELDS = List.of(
            "id",
            "iataCode",
            "icaoCode",
            "name",
            "country",
            "airlineStatus",
            "alliance",
            "createdAt",
            "updatedAt"
    );

    // Create Airline
    @PostMapping
    public ApiResponse<AirlineResponse> createAirline(@Valid @RequestBody AirlineRequest airlineRequest){
        return ApiResponse.success(airlineService.createAirline(airlineRequest, airlineRequest.getOwnerId()));
    }

    // Get Airline By Id
    @GetMapping("/{id}")
    public ApiResponse<AirlineResponse> getById(@PathVariable Long id){
        return ApiResponse.success(airlineService.getAirlineById(id));
    }

    // Get Airline By Owner
    @GetMapping("/owner/{ownerId}")
    public ApiResponse<AirlineResponse> getAirlineByOwner(@PathVariable Long ownerId) {
        return ApiResponse.success(airlineService.getAirlineByOwner(ownerId));
    }

    // Get All Airlines
    @GetMapping
    public ApiResponse<Page<AirlineResponse>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
                                                                                @RequestParam(defaultValue = "name") String sortBy, @RequestParam(defaultValue = "asc") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ApiResponse.success(airlineService.getAllAirlines(pageable));
    }

    // Search Airlines
    @GetMapping("/search")
    public ApiResponse<Page<AirlineResponse>> searchAirline(@RequestParam String keyword,
                                                            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size,
                                                            @RequestParam(defaultValue = "name") String sortBy, @RequestParam(defaultValue = "asc") String direction){
        Pageable pageable = PageRequest.of(page,size);
        return ApiResponse.success(airlineService.searchAirlines(keyword, pageable));
    }

    // Update Airline
    @PutMapping("/{airlineId}")
    public ApiResponse<AirlineResponse> updateAirline(@PathVariable Long airlineId, @Valid @RequestBody AirlineRequest airlineRequest, @RequestParam Long ownerId){
        return ApiResponse.success(airlineService.updateAirline(airlineId, airlineRequest, ownerId));
    }

    // Change Status - Admin
    @PatchMapping("/{airlineId}/status")
    public ApiResponse<AirlineResponse> changeStatus(@PathVariable Long airlineId, @RequestParam AirlineStatus airlineStatus){
        return ApiResponse.success(airlineService.changeStatusByAdmin(airlineId, airlineStatus));
    }

    // Delete Airline
    @DeleteMapping("/{airlineId}")
    public ApiResponse<Void> deleteAirline(@PathVariable Long airlineId, @RequestParam Long ownerId){
        airlineService.deleteAirline(airlineId, ownerId);
        return ApiResponse.success(null);
    }

    // Airline Dropdown
    @GetMapping("/dropdown")
    public ApiResponse<List<AirlineDropdownItem>> getDropdown(){
        return ApiResponse.success(airlineService.getAirlineDropdown());
    }



    private Pageable createPageable(int page, int size, String sortBy, String direction){
        if(page < 0){
            throw new IllegalArgumentException("Page cannot be negative");
        }
        if(size < 1){
            throw new IllegalArgumentException("Page size must be greater than zero");
        }

        if(size > MAX_PAGE_SIZE){
            throw new IllegalArgumentException("Page size cannot exceed " + MAX_PAGE_SIZE);
        }

        String validatedSortBy = validateSortField(sortBy);
        Sort.Direction sortDirection = parseSortDirection(direction);

        Sort sort = Sort.by(sortDirection, validatedSortBy);
        return PageRequest.of(page, size, sort);
    }

    private String validateSortField(String sortBy){
        if(sortBy == null || sortBy.isBlank()){
            return "name";
        }

        if(!ALLOWED_SORT_FIELDS.contains(sortBy)){
            throw new IllegalArgumentException("Invalid sort field: " + sortBy);
        }

        return sortBy;
    }

    private Sort.Direction parseSortDirection(String direction){
        if(direction == null || direction.isBlank()){
            return Sort.Direction.ASC;
        }

        try {
            return Sort.Direction.fromString(direction);
        }
        catch (IllegalArgumentException ex){
            throw new IllegalArgumentException("Invalid sort direction: " + direction + ". Use 'asc' or 'desc'.");
        }
    }


}
