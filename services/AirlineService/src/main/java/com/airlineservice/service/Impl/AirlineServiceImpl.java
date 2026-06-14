package com.airlineservice.service.Impl;

import com.airlineservice.mapper.AirlineMapper;
import com.airlineservice.model.Airline;
import com.airlineservice.repository.AirlineRepository;
import com.airlineservice.service.AirlineService;
import com.microservices.exception.ResourceAlreadyExistsException;
import com.microservices.exception.ResourceNotFoundException;
import com.microservices.payload.request.Airlines.Airline.AirlineRequest;
import com.microservices.payload.response.Airline.AirlineDropdownItem;
import com.microservices.payload.response.Airline.AirlineResponse;
import com.microservices.utils.Airline.AirlineStatus;
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

    private Airline findAirlineById(Long id){
        return airlineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found with id: " + id));
    }

    private Airline findAirlineByOwnerId(Long ownerId){
        return airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found with owner id: " + ownerId));
    }

    @Transactional
    @Override
    public AirlineResponse createAirline(AirlineRequest airlineRequest, Long ownerId) {
        if (airlineRepository.findByOwnerId(ownerId).isPresent()) {
            throw new ResourceAlreadyExistsException("Owner already has an airline");
        }
        if (airlineRepository.existsByIataCode(airlineRequest.getIataCode().toUpperCase())) {
            throw new ResourceAlreadyExistsException("Airline with IATA code already exists");
        }
        if (airlineRepository.existsByIcaoCode(airlineRequest.getIcaoCode().toUpperCase())) {
            throw new ResourceAlreadyExistsException("Airline with ICAO code already exists");
        }

        Airline airline = AirlineMapper.toEntity(airlineRequest, ownerId);
        if(airline.getAirlineStatus() == null){
            airline.setAirlineStatus(AirlineStatus.ACTIVE);
        }

        Airline savedAirline = airlineRepository.save(airline);
        return AirlineMapper.toResponse(savedAirline);
    }

    @Override
    public AirlineResponse getAirlineByOwner(Long ownerId) {
        Airline airline = findAirlineByOwnerId(ownerId);

        return AirlineMapper.toResponse(airline);
    }

    @Override
    public AirlineResponse getAirlineById(Long id) {
        Airline airline = findAirlineById(id);

        return AirlineMapper.toResponse(airline);
    }

    @Override
    public Page<AirlineResponse> getAllAirlines(Pageable pageable) {
        return airlineRepository.findAll(pageable)
                .map(AirlineMapper::toResponse);
    }

    @Transactional
    @Override
    public AirlineResponse updateAirline(Long airlineId, AirlineRequest airlineRequest, Long ownerId) {
        Airline airline = findAirlineById(airlineId);
        if(!airline.getOwnerId().equals(ownerId)) {
            throw new ResourceNotFoundException("You are not allowed to update this airline");
        }
        if(airlineRepository.existsByIataCodeAndIdNot(airlineRequest.getIataCode().toUpperCase(), airlineId)) {
            throw new ResourceAlreadyExistsException("Airline with IATA code already exists");
        }
        if(airlineRepository.existsByIcaoCodeAndIdNot(airlineRequest.getIcaoCode().toUpperCase(), airlineId)) {
            throw new ResourceAlreadyExistsException("Airline with ICAO code already exists");
        }

        AirlineMapper.updateEntity(airline, airlineRequest);
        Airline updatedAirline = airlineRepository.save(airline);

        return AirlineMapper.toResponse(updatedAirline);
    }

    @Transactional
    @Override
    public void deleteAirline(Long id, Long ownerId) {
        Airline airline = findAirlineById(id);

        if (!airline.getOwnerId().equals(ownerId)) {
            throw new ResourceNotFoundException(
                    "You are not allowed to delete this airline");
        }

        airlineRepository.delete(airline);
    }

    @Transactional
    @Override
    public AirlineResponse changeStatusByAdmin(Long airlineId, AirlineStatus airlineStatus) {
        Airline airline = findAirlineById(airlineId);

        airline.setAirlineStatus(airlineStatus);
        Airline updatedAirline = airlineRepository.save(airline);

        return AirlineMapper.toResponse(updatedAirline);
    }

    @Override
    public List<AirlineDropdownItem> getAirlineDropdown() {
        return airlineRepository.findAll().stream()
                .map(AirlineMapper::toDropdown)
                .collect(Collectors.toList());
    }
}
