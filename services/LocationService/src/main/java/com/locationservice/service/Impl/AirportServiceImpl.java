package com.locationservice.service.Impl;

import com.airlineportal.exception.ResourceAlreadyExistsException;
import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Location.Airport.AirportRequest;
import com.airlineportal.payload.response.Location.Airport.AirportResponse;
import com.locationservice.helper.AirportHelper;
import com.locationservice.mapper.AirportMapper;
import com.locationservice.model.Airport;
import com.locationservice.model.City;
import com.locationservice.repository.AirportRepository;
import com.locationservice.repository.CityRepository;
import com.locationservice.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AirportServiceImpl implements AirportService {
    private final AirportRepository airportRepository;
    private final AirportHelper helper;

    @Override
    @Transactional
    public AirportResponse createAirport(AirportRequest airportRequest) {
        helper.normalizeRequest(airportRequest);

        helper.validateIataCodeForCreate(airportRequest.getIataCode());

        City city = helper.findCityById(airportRequest.getCityId());
        Airport airport = AirportMapper.toEntity(airportRequest, city);

        Airport savedAirport = airportRepository.save(airport);
        return AirportMapper.toResponse(savedAirport);
    }

    @Override
    public AirportResponse getAirportById(Long id) {
        Airport airport = helper.findAirportById(id);

        return AirportMapper.toResponse(airport);
    }

    @Override
    public AirportResponse getAirportByIataCode(String iataCode) {
        Airport airport = helper.findAirportByIataCode(iataCode);

        return AirportMapper.toResponse(airport);
    }

    @Override
    @Transactional
    public AirportResponse updateAirport(Long id, AirportRequest airportRequest) {
        Airport airport = helper.findAirportById(id);

        helper.normalizeRequest(airportRequest);
        helper.validateIataCodeForUpdate(airportRequest.getIataCode(), id);

        City city = helper.findCityById(airportRequest.getCityId());
        AirportMapper.updateEntity(airport, airportRequest, city);

        Airport updatedAirport = airportRepository.save(airport);
        return AirportMapper.toResponse(updatedAirport);
    }

    @Override
    public void deleteAirport(Long id) {
        Airport airport = helper.findAirportById(id);

        airportRepository.delete(airport);
    }

    @Override
    public Page<AirportResponse> getAllAirports(Pageable pageable) {
        return airportRepository.findAll(pageable)
                .map(AirportMapper::toResponse);
    }

    @Override
    public Page<AirportResponse> searchAirports(String keyword, Pageable pageable) {
        if(keyword == null || keyword.isBlank())
            return getAllAirports(pageable);

        return airportRepository.search(keyword.trim(), pageable)
                .map(AirportMapper::toResponse);
    }

    @Override
    public List<AirportResponse> getAirportsByCity(Long cityId) {
        helper.findCityById(cityId);

        return airportRepository.findByCityId(cityId).stream()
                .map(AirportMapper::toResponse)
                .toList();
    }

    @Override
    public Page<AirportResponse> getAirportsByCity(Long cityId, Pageable pageable) {
        helper.findCityById(cityId);

        return airportRepository.findByCityId(cityId, pageable)
                .map(AirportMapper::toResponse);
    }

    @Override
    public Page<AirportResponse> getAirportsByCountry(String countryCode, Pageable pageable) {
        String normalizedCountryCode = countryCode.trim().toUpperCase();

        return airportRepository.findByCityCountryCodeIgnoreCase(normalizedCountryCode, pageable)
                .map(AirportMapper::toResponse);
    }

    @Override
    public List<AirportResponse> getAirportDropdown() {
        return airportRepository.findDropdown().stream()
                .map(AirportMapper::toResponse)
                .toList();
    }

    @Override
    public boolean airportExists(String iataCode) {
        String normalizedCode = helper.normalizeIataCode(iataCode);
        if(normalizedCode == null || normalizedCode.isBlank())
            return false;

        return airportRepository.existsByIataCode(normalizedCode);
    }
}
