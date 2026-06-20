package com.locationservice.service.Impl;

import com.locationservice.mapper.CityMapper;
import com.locationservice.model.City;
import com.locationservice.repository.CityRepository;
import com.locationservice.service.CityService;
import com.microservices.exception.ResourceNotFoundException;
import com.microservices.payload.request.Location.City.CityRequest;
import com.microservices.payload.response.Location.City.CityResponse;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    private City findCityById(Long id){
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
    }

    @Transactional
    @Override
    public CityResponse createCity(CityRequest cityRequest){
        String code = cityRequest.getCityCode().toUpperCase().trim();
        if(cityRepository.existsByCityCode(cityRequest.getCityCode())){
            throw new ResourceNotFoundException("City with given code already exist");
        }

        City city = CityMapper.toEntity(cityRequest);
        city.setCityCode(code);

        City savedCity = cityRepository.save(city);
        return CityMapper.toResponse(savedCity);
    }

    @Override
    public CityResponse getCityById(Long id){
        City city = findCityById(id);
        return CityMapper.toResponse(city);
    }

    @Transactional
    @Override
    public CityResponse updateCity(Long id, CityRequest cityRequest){
        City city = findCityById(id);
        if(cityRequest.getCityCode() != null && cityRepository.existsByCityCodeAndIdNot(cityRequest.getCityCode(), id)){
            throw new ResourceNotFoundException("City with given code already exists");
        }

        CityMapper.updateEntity(city, cityRequest);
        City updatedCity = cityRepository.save(city);
        return CityMapper.toResponse(updatedCity);
    }

    @Transactional
    @Override
    public void deleteCity(Long id){
        City city = findCityById(id);
        cityRepository.delete(city);
    }

    @Override
    public Page<CityResponse> getAllCities(Pageable pageable) {
        return cityRepository.findAll(pageable)
                .map(CityMapper::toResponse);
    }

    @Override
    public Page<CityResponse> searchCities(String keyword, Pageable pageable) {
        return cityRepository.searchByKeyword(keyword, pageable)
                .map(CityMapper::toResponse);
    }

    @Override
    public Page<CityResponse> getCitiesByCountryCode(String countryCode, Pageable pageable) {
        return cityRepository.findByCountryCodeIgnoreCase(countryCode, pageable)
                .map(CityMapper::toResponse);
    }

    @Override
    public List<CityResponse> getCityDropdown() {
        return List.of();
    }

    @Override
    public CityResponse getCityByCode(String cityCode){
        City city = cityRepository.findByCityCode(cityCode)
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));
        return CityMapper.toResponse(city);
    }

    @Override
    public boolean cityExists(String cityCode) {
        return cityRepository.existsByCityCode(cityCode);
    }


}
