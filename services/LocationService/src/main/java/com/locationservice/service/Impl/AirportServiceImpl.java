package com.locationservice.service.Impl;

import com.locationservice.mapper.AirportMapper;
import com.locationservice.model.Airport;
import com.locationservice.model.City;
import com.locationservice.repository.AirportRepository;
import com.locationservice.repository.CityRepository;
import com.locationservice.service.AirportService;
import com.microservices.exception.ResourceAlreadyExistsException;
import com.microservices.exception.ResourceNotFoundException;
import com.microservices.payload.request.Location.Airport.AirportRequest;
import com.microservices.payload.response.Airport.AirportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AirportServiceImpl implements AirportService {
    private final AirportRepository airportRepository;
    private final CityRepository cityRepository;

    private Airport findAirportById(Long id){
        return airportRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Airport not exist with id: " + id));
    }
    private City findCityById(Long id){
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
    }


    @Transactional
    @Override
    public AirportResponse createAirport(AirportRequest airportRequest){
        if (airportRepository.existsByIataCode(airportRequest.getIataCode())) {
            throw new ResourceAlreadyExistsException("Airport with IATA code already exists");
        }
        City city = findCityById(airportRequest.getCityId());

        Airport airport = AirportMapper.toEntity(airportRequest);
        airport.setCity(city);
        Airport savedAirport = airportRepository.save(airport);
        return AirportMapper.toResponse(savedAirport);
    }

    @Override
    public AirportResponse getAirportById(Long id){
        Airport airport = findAirportById(id);
        return AirportMapper.toResponse(airport);
    }

    @Override
    public AirportResponse getAirportByIataCode(String iataCode){
        Airport airport = airportRepository.findByIataCode(iataCode)
                .orElseThrow(() -> new ResourceNotFoundException("Airport not found with IATA code: " + iataCode));
        return AirportMapper.toResponse(airport);
    }

    @Override
    public Page<AirportResponse> getAllAirports(Pageable pageable) {
        return airportRepository.findAll(pageable)
                .map(AirportMapper::toResponse);
    }

    @Override
    public Page<AirportResponse> searchAirports(String keyword, Pageable pageable){
        return airportRepository.search(keyword, pageable)
                .map(AirportMapper::toResponse);
    }

    @Override
    public Page<AirportResponse> getAirportByCityId(Long cityId, Pageable pageable) {
        return airportRepository.findByCityId(cityId, pageable)
                .map(AirportMapper::toResponse);
    }

    @Transactional
    @Override
    public AirportResponse updateAirport(Long id, AirportRequest airportRequest){
        Airport airport = findAirportById(id);
        if (airportRepository.existsByIataCodeAndIdNot(airportRequest.getIataCode(), id)) {
            throw new ResourceAlreadyExistsException("Airport with IATA code already exists");
        }
        if (!airport.getCity().getId().equals(airportRequest.getCityId())) {
            City city = findCityById(airportRequest.getCityId());
            airport.setCity(city);
        }

        AirportMapper.updateEntity(airport, airportRequest);
        Airport updatedAirport = airportRepository.save(airport);
        return AirportMapper.toResponse(updatedAirport);
    }

    @Transactional
    @Override
    public void deleteAirport(Long id){
        Airport airport = findAirportById(id);
        airportRepository.delete(airport);
    }


}
