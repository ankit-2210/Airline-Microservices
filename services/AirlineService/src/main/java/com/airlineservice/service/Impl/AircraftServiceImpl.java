package com.airlineservice.service.Impl;

import com.airlineservice.mapper.AircraftMapper;
import com.airlineservice.model.Aircraft;
import com.airlineservice.model.Airline;
import com.airlineservice.repository.AircraftRepository;
import com.airlineservice.repository.AirlineRepository;
import com.airlineservice.service.AircraftService;
import com.microservices.exception.ResourceAlreadyExistsException;
import com.microservices.exception.ResourceNotFoundException;
import com.microservices.payload.request.Airlines.Aircraft.AircraftRequest;
import com.microservices.payload.response.Airline.AircraftResponse;
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
    private final AirlineRepository airlineRepository;

    private Aircraft findAircraftById(Long id){
        return aircraftRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException( "Aircraft not found with id: " + id));
    }
    private Airline findAirlineByOwner(Long ownerId) {
        return airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Airline not found for owner: " + ownerId));
    }

    @Transactional
    @Override
    public AircraftResponse createAircraft(AircraftRequest aircraftRequest, Long ownerId) {
        if(aircraftRepository.existsByCode(aircraftRequest.getCode().toUpperCase())){
            throw new ResourceAlreadyExistsException("Aircraft code already exists");
        }

        Airline airline = findAirlineByOwner(ownerId);
        Aircraft aircraft = AircraftMapper.toEntity(aircraftRequest, airline);

        Aircraft savedAircraft = aircraftRepository.save(aircraft);
        return AircraftMapper.toResponse(savedAircraft);
    }

    @Override
    public AircraftResponse getById(Long id) {
        Aircraft aircraft = findAircraftById(id);
        return AircraftMapper.toResponse(aircraft);
    }

    @Override
    public Page<AircraftResponse> allAircraftByOwner(Long ownerId, Pageable pageable) {
        return aircraftRepository.findByAirlineOwnerId(ownerId, pageable)
                .map(AircraftMapper::toResponse);
    }

    @Transactional
    @Override
    public AircraftResponse updateAircraft(Long id, AircraftRequest aircraftRequest, Long ownerId) {
        Aircraft aircraft = findAircraftById(id);
        if (!aircraft.getAirline().getOwnerId().equals(ownerId)) {
            throw new ResourceNotFoundException("You are not authorized to update this aircraft");
        }
        if(aircraftRepository.existsByCodeAndIdNot(aircraftRequest.getCode().toUpperCase(), id)) {
            throw new ResourceAlreadyExistsException("Aircraft code already exists");
        }

        AircraftMapper.updateEntity(aircraft, aircraftRequest);
        Aircraft updatedAircraft = aircraftRepository.save(aircraft);
        return AircraftMapper.toResponse(updatedAircraft);
    }

    @Transactional
    @Override
    public void deleteAircraft(Long id, Long ownerId) {
        Aircraft aircraft = findAircraftById(id);
        if(!aircraft.getAirline().getOwnerId().equals(ownerId)){
            throw new ResourceNotFoundException("You are not authorized to delete this aircraft");
        }
        aircraftRepository.delete(aircraft);

    }
}
