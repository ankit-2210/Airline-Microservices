package com.airlineservice.service.Impl;

import com.airlineportal.payload.request.Airlines.Airline.AirlineRequest;
import com.airlineportal.payload.response.Airlines.Airline.AirlineDropdownItem;
import com.airlineportal.payload.response.Airlines.Airline.AirlineResponse;
import com.airlineportal.utils.Airline.AirlineStatus;
import com.airlineservice.helper.AirlineHelper;
import com.airlineservice.mapper.AirlineMapper;
import com.airlineservice.model.Airline;
import com.airlineservice.repository.AirlineRepository;
import com.airlineservice.service.AirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AirlineServiceImpl implements AirlineService {
    private final AirlineRepository airlineRepository;
    private final AirlineHelper airlineHelper;

    // Create Airline
    @Override
    @Transactional
    public AirlineResponse createAirline(AirlineRequest airlineRequest, Long ownerId){
        airlineHelper.validateCreate(airlineRequest, ownerId);

        Airline airline = AirlineMapper.toEntity(airlineRequest, ownerId);
        airlineHelper.normalizeEntity(airline);

        if(airline.getAirlineStatus() == null){
            airline.setAirlineStatus(AirlineStatus.ACTIVE);
        }

        Airline savedAirline = airlineRepository.save(airline);
        return AirlineMapper.toResponse(savedAirline);
    }

    // Get Airline By Owner
    @Override
    public AirlineResponse getAirlineByOwner(Long ownerId) {
        Airline airline = airlineHelper.findByOwnerId(ownerId);
        return AirlineMapper.toResponse(airline);
    }

    // Get Airline By Id
    @Override
    public AirlineResponse getAirlineById(Long id) {
        Airline airline = airlineHelper.findById(id);
        return AirlineMapper.toResponse(airline);
    }

    // Get All Airlines
    @Override
    public Page<AirlineResponse> getAllAirlines(Pageable pageable) {
        return airlineRepository.findAll(pageable)
                .map(AirlineMapper::toResponse);
    }

    // Search Airlines
    @Override
    public Page<AirlineResponse> searchAirlines(String keyword, Pageable pageable) {
        if(keyword == null || keyword.isBlank()){
            return airlineRepository.findAll(pageable)
                    .map((AirlineMapper::toResponse));
        }

        String search = keyword.trim();
        return airlineRepository.findByNameContainingIgnoreCase(search, pageable)
                .map(AirlineMapper::toResponse);
    }

    // Update Airline
    @Override
    @Transactional
    public AirlineResponse updateAirline(Long airlineId, AirlineRequest airlineRequest, Long ownerId) {
        Airline airline = airlineHelper.findById(airlineId);

        airlineHelper.validateUpdate(airline, airlineRequest, ownerId);

        AirlineMapper.updateEntity(airline, airlineRequest);
        airlineHelper.normalizeEntity(airline);

        Airline updatedAirline = airlineRepository.save(airline);
        return AirlineMapper.toResponse(updatedAirline);
    }

    // Delete Airline
    @Override
    @Transactional
    public void deleteAirline(Long id, Long ownerId) {
        Airline airline = airlineHelper.findById(id);

        airlineHelper.validateOwnership(airline, ownerId);
        if(airline.getAircraft() != null && !airline.getAircraft().isEmpty()){
            throw new IllegalStateException("Cannot delete airline while aircraft are assigned to it");
        }

        airlineRepository.delete(airline);
    }

    // Change Status = Admin
    @Override
    @Transactional
    public AirlineResponse changeStatusByAdmin(Long airlineId, AirlineStatus airlineStatus) {
        if (airlineStatus == null) {
            throw new IllegalArgumentException("Airline status cannot be null");
        }

        Airline airline = airlineHelper.findById(airlineId);
        airline.setAirlineStatus(airlineStatus);

        Airline updatedAirline = airlineRepository.save(airline);
        return AirlineMapper.toResponse(updatedAirline);
    }

    // Dropdown
    @Override
    public List<AirlineDropdownItem> getAirlineDropdown() {
        return airlineRepository
                .findAll()
                .stream()
                .map(AirlineMapper::toDropdown)
                .toList();

    }
}
