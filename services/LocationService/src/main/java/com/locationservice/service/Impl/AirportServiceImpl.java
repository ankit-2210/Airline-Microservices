package com.locationservice.service.Impl;

import com.locationservice.mapper.AirportMapper;
import com.locationservice.model.Airport;
import com.locationservice.model.City;
import com.locationservice.repository.AirportRepository;
import com.locationservice.repository.CityRepository;
import com.locationservice.service.AirportService;
import com.microservices.exception.ResourceAlreadyExistsException;
import com.microservices.exception.ResourceNotFoundException;
import com.microservices.payload.request.Airport.AirportRequest;
import com.microservices.payload.response.Airport.AirportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {
    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;

    @Override
    public AirportResponse createAirport(AirportRequest airportRequest){
        if(airportRepository.findByIataCode(airportRequest.getIataCode()).isPresent()){
            throw new ResourceAlreadyExistsException("Airport with iata Code is already present");
        }

        City city = cityRepository.findById(airportRequest.getCityId())
                .orElseThrow(()-> new ResourceNotFoundException("City not found"));
        Airport airport = AirportMapper.toEntity(airportRequest);
        airport.setCity(city);
        Airport savedAirport = airportRepository.save(airport);
        return AirportMapper.toResponse(savedAirport);
    }

    @Override
    public AirportResponse getAirportById(Long id){
        Airport airport = airportRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Airport not exist with id: " + id));
        return AirportMapper.toResponse(airport);
    }

    @Override
    public List<AirportResponse> getAllAirports() {
        return airportRepository.findAll().stream()
                .map(AirportMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AirportResponse updateAirport(Long id, AirportRequest airportRequest){
        Airport airport = airportRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Airport not exist with id: " + id));

        if(airportRequest.getIataCode() != null &&
            !airport.getIataCode().equals(airportRequest.getIataCode()) &&
            airportRepository.findByIataCode(airportRequest.getIataCode()).isPresent()){
            throw new ResourceAlreadyExistsException("Airport with iata code already exist");
        }

        AirportMapper.updateEntity(airport, airportRequest);
        Airport updatedAirport = airportRepository.save(airport);
        return AirportMapper.toResponse(updatedAirport);
    }

    @Override
    public void deleteAirport(Long id){
        Airport airport = airportRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Airport not exist with id"));
        airportRepository.delete(airport);
    }

    @Override
    public List<AirportResponse> getAirportByCityId(Long cityId) {
        return airportRepository.findByCityId(cityId).stream()
                .map(AirportMapper::toResponse)
                .collect(Collectors.toList());
    }
}
