package com.airlineservice.service.Impl;

import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Airlines.Aircraft.AircraftRequest;
import com.airlineportal.payload.response.Airlines.Aircraft.AircraftResponse;
import com.airlineportal.utils.Airline.AircraftStatus;
import com.airlineservice.helper.AircraftHelper;
import com.airlineservice.mapper.AircraftMapper;
import com.airlineservice.model.Aircraft;
import com.airlineservice.model.Airline;
import com.airlineservice.repository.AircraftRepository;
import com.airlineservice.repository.AirlineRepository;
import com.airlineservice.service.AircraftService;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AircraftServiceImpl implements AircraftService {
    private final AircraftRepository aircraftRepository;
    private final AircraftHelper aircraftHelper;

    // Create Aircraft
    @Transactional
    @Override
    public AircraftResponse createAircraft(AircraftRequest aircraftRequest, Long ownerId) {
        aircraftHelper.validateCreate(aircraftRequest, ownerId);

        Airline airline = aircraftHelper.findAirlineByOwner(ownerId);
        Aircraft aircraft = AircraftMapper.toEntity(aircraftRequest, airline);

        aircraftHelper.normalizeEntity(aircraft);

        Aircraft savedAircraft = aircraftRepository.save(aircraft);
        return AircraftMapper.toResponse(savedAircraft);
    }

    // Get Aircraft By Id
    @Override
    public AircraftResponse getById(Long id) {
        Aircraft aircraft = aircraftHelper.findById(id);
        return AircraftMapper.toResponse(aircraft);
    }

    // Get Aircraft By Owner
    @Override
    public Page<AircraftResponse> allAircraftByOwner(Long ownerId, Pageable pageable) {
        if(ownerId == null){
            throw new IllegalArgumentException("Owner id cannot be null");
        }

        return aircraftRepository.findByAirlineOwnerId(ownerId, pageable)
                .map(AircraftMapper::toResponse);
    }

    // Get Aircraft By Airline
    @Override
    public Page<AircraftResponse> getAircraftByAirline(Long airlineId, Pageable pageable) {
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }

        return aircraftRepository.findByAirlineId(airlineId, pageable)
                .map(AircraftMapper::toResponse);
    }

    // Search Aircraft
    @Override
    public Page<AircraftResponse> searchAircraft(String keyword, Pageable pageable) {
        if(keyword == null || keyword.isBlank())
            throw new IllegalArgumentException("Search keyword cannot be blank");

        String searchKeyword = keyword.trim();
        return aircraftRepository.findByCodeContainingIgnoreCaseOrModelContainingIgnoreCaseOrManufacturerContainingIgnoreCase(
                searchKeyword,
                searchKeyword,
                searchKeyword,
                pageable)
                .map(AircraftMapper::toResponse);
    }

    // Get Aircraft By Airport
    @Override
    public Page<AircraftResponse> getAircraftByAirport(Long airlineId, Pageable pageable){
        if(airlineId == null){
            throw new IllegalArgumentException("Airline id cannot be null");
        }

        return aircraftRepository.findByCurrentAirportId(airlineId, pageable)
                .map(AircraftMapper::toResponse);
    }

    // Get Available Aircraft
    @Override
    public Page<AircraftResponse> getAvailableAircraft(Pageable pageable){
        return aircraftRepository.findByIsAvailableTrue(pageable)
                .map(AircraftMapper::toResponse);
    }

    // Update Aircraft
    @Transactional
    @Override
    public AircraftResponse updateAircraft(Long id, AircraftRequest aircraftRequest, Long ownerId) {
        Aircraft aircraft = aircraftHelper.findById(id);

        aircraftHelper.validateUpdate(aircraft, aircraftRequest, ownerId);

        AircraftMapper.updateEntity(aircraft, aircraftRequest);
        aircraftHelper.normalizeEntity(aircraft);

        Aircraft updatedAircraft = aircraftRepository.save(aircraft);
        return AircraftMapper.toResponse(updatedAircraft);
    }

    // Delete Aircraft
    @Transactional
    @Override
    public void deleteAircraft(Long id, Long ownerId) {
        Aircraft aircraft = aircraftHelper.findById(id);

        aircraftHelper.validateOwnership(aircraft, ownerId);
        aircraftHelper.validateCanDelete(aircraft);

        aircraftRepository.delete(aircraft);
    }


}
