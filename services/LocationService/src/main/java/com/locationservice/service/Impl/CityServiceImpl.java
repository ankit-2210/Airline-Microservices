package com.locationservice.service.Impl;

import com.airlineportal.payload.request.Location.City.CityRequest;
import com.airlineportal.payload.response.Location.City.CityResponse;
import com.locationservice.helper.CityHelper;
import com.locationservice.mapper.CityMapper;
import com.locationservice.model.City;
import com.locationservice.repository.CityRepository;
import com.locationservice.service.CityService;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final CityHelper helper;

    @Override
    @Transactional
    public CityResponse createCity(CityRequest cityRequest) {
        helper.normalizeRequest(cityRequest);

        helper.validateCityCodeForCreate(cityRequest.getCityCode());

        City city = CityMapper.toEntity(cityRequest);
        return CityMapper.toResponse(cityRepository.save(city));
    }

    @Override
    public CityResponse getCityById(Long id) {
        return CityMapper.toResponse(helper.findCityById(id));
    }

    @Override
    public CityResponse getCityByCode(String cityCode) {
        return CityMapper.toResponse(helper.findCityByCode(cityCode));
    }

    @Override
    @Transactional
    public CityResponse updateCity(Long id, CityRequest cityRequest) {
        City city = helper.findCityById(id);

        helper.normalizeRequest(cityRequest);
        helper.validateCityCodeForUpdate(cityRequest.getCityCode(), id);

        CityMapper.updateEntity(city, cityRequest);
        return CityMapper.toResponse(cityRepository.save(city));
    }

    @Override
    @Transactional
    public void deleteCity(Long id) {
        City city = helper.findCityById(id);
        cityRepository.delete(city);
    }

    @Override
    public Page<CityResponse> getAllCities(Pageable pageable) {
        return cityRepository.findAll(pageable)
                .map(CityMapper::toResponse);
    }

    @Override
    public Page<CityResponse> searchCities(String keyword, Pageable pageable) {
        if(keyword == null || keyword.isBlank())
            return getAllCities(pageable);

        return cityRepository.searchByKeyword(keyword.trim(), pageable)
                .map(CityMapper::toResponse);
    }

    @Override
    public Page<CityResponse> getCitiesByCountryCode(String countryCode, Pageable pageable) {
        String normalizedCountryCode = helper.normalizeCountryCode(countryCode);

        return cityRepository.findByCountryCodeIgnoreCase(normalizedCountryCode, pageable)
                .map(CityMapper::toResponse);
    }

    @Override
    public List<CityResponse> getCityDropdown() {
        return cityRepository.findAll().stream()
                .map(CityMapper::toResponse)
                .toList();
    }

    @Override
    public boolean cityExists(String cityCode) {
        return helper.normalizeCityCode(cityCode) != null &&
                cityRepository.existsByCityCode(helper.normalizeCityCode(cityCode));
    }
}
