package com.mateusz.jakuszko.roomforyou.facade.openweather;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.dto.TemperatureDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import com.mateusz.jakuszko.roomforyou.openweatherapi.OpenWeatherApiClient;
import com.mateusz.jakuszko.roomforyou.openweatherapi.TemperatureMapper;
import com.mateusz.jakuszko.roomforyou.openweatherapi.dto.GetTemperatureResponse;
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenWeatherFacade {
    private final OpenWeatherApiClient openWeatherApiClient;
    private final TemperatureMapper temperatureMapper;
    private final ApartmentDbService apartmentDbService;

    public TemperatureDto getTemperaturesFromWeatherApiResponse(Long apartmentId) {
        log.info("Get apartment from db with id - " + apartmentId);
        Apartment apartment = apartmentDbService.getApartment(apartmentId).orElseThrow(NotFoundException::new);
        ApartmentDto apartmentDto = new ApartmentDto.Builder()
                .latitude(apartment.getLatitude())
                .longitude(apartment.getLongitude()).build();
        GetTemperatureResponse temperatureResponse = openWeatherApiClient.getTemperatureResponse(apartmentDto);
        return temperatureMapper.mapToTemperaturesDto(temperatureResponse);
    }
}
