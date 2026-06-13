package com.locationservice.service.Impl;

import com.locationservice.mapper.CityMapper;
import com.locationservice.model.City;
import com.locationservice.repository.CityRepository;
import com.locationservice.service.CityService;
import com.microservices.exception.ResourceNotFoundException;
import com.microservices.payload.request.City.CityRequest;
import com.microservices.payload.response.City.CityResponse;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public CityResponse createCity(CityRequest cityRequest) throws Exception{
        if(cityRepository.existsByCityCode(cityRequest.getCityCode())){
            throw new Exception("City with given code already exist");
        }
        City city = CityMapper.toEntity(cityRequest);
        City savedCity = cityRepository.save(city);
        return CityMapper.toResponse(savedCity);
    }

    @Override
    public CityResponse getCityById(Long id) throws Exception{
        City city = findCityById(id);
        return CityMapper.toResponse(city);
    }

    @Transactional
    @Override
    public CityResponse updateCity(Long id, CityRequest cityRequest) throws Exception{
        City city = findCityById(id);
        if(cityRepository.existsByCityCodeAndIdNot(cityRequest.getCityCode(), id)){
            throw new Exception("City with given code already exists");
        }
        CityMapper.updateEntity(city, cityRequest);
        City updatedCity = cityRepository.save(city);
        return CityMapper.toResponse(updatedCity);
    }

    @Transactional
    @Override
    public void deleteCity(Long id) throws Exception{
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
    public boolean cityExists(String cityCode) {
        return cityRepository.existsByCityCode(cityCode);
    }


}
