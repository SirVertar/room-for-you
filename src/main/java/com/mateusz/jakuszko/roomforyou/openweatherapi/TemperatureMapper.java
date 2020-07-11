package com.mateusz.jakuszko.roomforyou.openweatherapi;

import com.mateusz.jakuszko.roomforyou.dto.TemperatureDto;
import com.mateusz.jakuszko.roomforyou.openweatherapi.dto.GetTemperatureResponse;
import com.mateusz.jakuszko.roomforyou.openweatherapi.dto.TemperatureResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class TemperatureMapper {

    public TemperatureDto mapToTemperaturesDto(GetTemperatureResponse temperatures) {
        log.info("Map response from weather api to TemperatureDto object");
        TemperatureResponse temperatureResponse = temperatures.getTemperature().get(0).getTemperatureResponse();
        temperatureResponse.setTemp(temperatureResponse.getTemp());
        return TemperatureDto.builder()
                .temp(mapToCelsius(temperatures.getTemperature().get(0).getTemperatureResponse().getTemp())).build();
    }

    private BigDecimal mapToCelsius(BigDecimal temp) {
        log.info("Change temperature from Kelvin to Celsius");
        return temp.subtract(BigDecimal.valueOf(273.15));
    }
}
