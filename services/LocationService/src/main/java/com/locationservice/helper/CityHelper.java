package com.locationservice.helper;

import com.airlineportal.exception.ResourceNotFoundException;
import com.airlineportal.payload.request.Location.City.CityRequest;
import com.locationservice.model.City;
import com.locationservice.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CityHelper {
    private final CityRepository cityRepository;

    public void normalizeRequest(CityRequest request) {
        if(request == null)
            return;

        if(request.getName() != null){
            request.setName(request.getName().trim());
        }
        if(request.getCityCode() != null){
            request.setCityCode(request.getCityCode().trim().toUpperCase());
        }
        if(request.getCountryCode() != null){
            request.setCountryCode(request.getCountryCode().trim().toUpperCase());
        }
        if(request.getCountryName() != null){
            request.setCountryName(request.getCountryName().trim());
        }
        if(request.getRegionCode() != null){
            request.setRegionCode(request.getRegionCode().trim().toUpperCase());
        }
        if(request.getTimeZoneId() != null){
            request.setTimeZoneId(request.getTimeZoneId().trim());
        }
    }

    public void validateCityCodeForCreate(String cityCode){
        if(cityCode == null || cityCode.isBlank())
            return;

        String normalizedCode = cityCode.trim().toUpperCase();
        if(cityRepository.existsByCityCode(normalizedCode)){
            throw new IllegalArgumentException("City with code '" + normalizedCode + "' already exists");
        }
    }

    public void validateCityCodeForUpdate(String cityCode, Long cityId){
        if(cityCode == null || cityCode.isBlank())
            return;

        String normalizedCode = cityCode.trim().toUpperCase();
        if(cityRepository.existsByCityCodeAndIdNot(normalizedCode, cityId)){
            throw new IllegalArgumentException("City with code '" + normalizedCode + "' already exists");
        }
    }


    public City findCityById(Long id){
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + id));
    }

    public City findCityByCode(String cityCode) {
        String normalizedCode = cityCode.trim().toUpperCase();

        return cityRepository.findByCityCode(normalizedCode)
                .orElseThrow(() -> new ResourceNotFoundException("City not found with code: " + normalizedCode));
    }

    public String normalizeCityCode(String cityCode){
        if(cityCode == null)
            return null;

        return cityCode.trim().toUpperCase();
    }

    public String normalizeCountryCode(String countryCode){
        if(countryCode == null)
            return null;

        return countryCode.trim().toUpperCase();
    }

}
