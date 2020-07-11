package com.mateusz.jakuszko.roomforyou.openweatherapi;

import com.mateusz.jakuszko.roomforyou.dto.TemperatureDto;
import com.mateusz.jakuszko.roomforyou.openweatherapi.dto.GetTemperatureResponse;
import com.mateusz.jakuszko.roomforyou.openweatherapi.dto.Temperature;
import com.mateusz.jakuszko.roomforyou.openweatherapi.dto.TemperatureResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TemperatureMapperTest {
    @Autowired
    private TemperatureMapper temperatureMapper;

    @Test
    public void whenMapGetTemperatureResponseToTemperatureDtoThenReturnCorrectTemperatureInCelsius() {
        //Given
        TemperatureResponse temperatureResponse1 = TemperatureResponse.builder()
                .temp(BigDecimal.valueOf(273.15)).build();
        TemperatureResponse temperatureResponse2 = TemperatureResponse.builder()
                .temp(BigDecimal.valueOf(249.65)).build();
        TemperatureResponse temperatureResponse3 = TemperatureResponse.builder()
                .temp(BigDecimal.valueOf(307.15)).build();

        Temperature temperature1 = Temperature.builder().temperatureResponse(temperatureResponse1).build();
        Temperature temperature2 = Temperature.builder().temperatureResponse(temperatureResponse2).build();
        Temperature temperature3 = Temperature.builder().temperatureResponse(temperatureResponse3).build();

        List<Temperature> temperatures1 = new ArrayList<>();
        temperatures1.add(temperature1);
        List<Temperature> temperatures2 = new ArrayList<>();
        temperatures2.add(temperature2);
        List<Temperature> temperatures3 = new ArrayList<>();
        temperatures3.add(temperature3);

        GetTemperatureResponse getTemperatureResponse1 = GetTemperatureResponse.builder()
                .temperature(temperatures1).build();
        GetTemperatureResponse getTemperatureResponse2 = GetTemperatureResponse.builder()
                .temperature(temperatures2).build();
        GetTemperatureResponse getTemperatureResponse3 = GetTemperatureResponse.builder()
                .temperature(temperatures3).build();

        List<BigDecimal> expectedCelsiusTemperatures = new ArrayList<>();
        expectedCelsiusTemperatures.add(BigDecimal.ZERO);
        expectedCelsiusTemperatures.add(BigDecimal.valueOf(-23.50));
        expectedCelsiusTemperatures.add(BigDecimal.valueOf(34.00));
        //When
        TemperatureDto temperatureDto1 = temperatureMapper.mapToTemperaturesDto(getTemperatureResponse1);
        TemperatureDto temperatureDto2 = temperatureMapper.mapToTemperaturesDto(getTemperatureResponse2);
        TemperatureDto temperatureDto3 = temperatureMapper.mapToTemperaturesDto(getTemperatureResponse3);
        //Then
        System.out.println(temperatureDto1.getTemp());
        System.out.println(temperatureDto2.getTemp());
        System.out.println(temperatureDto3.getTemp());

        assertEquals(0, temperatureDto1.getTemp().compareTo(expectedCelsiusTemperatures.get(0)));
        assertEquals(0, temperatureDto2.getTemp().compareTo(expectedCelsiusTemperatures.get(1)));
        assertEquals(0, temperatureDto3.getTemp().compareTo(expectedCelsiusTemperatures.get(2)));
    }
}
