package com.mateusz.jakuszko.roomforyou.openweatherapi;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.openweatherapi.dto.GetTemperatureResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OpenWeatherApiClientTest {

    @Autowired
    private OpenWeatherApiClient openWeatherApiClient;
    @Autowired
    private OpenWeatherApiConfig openWeatherApiConfig;

    private ApartmentDto createApartment(Long id) {
        return new ApartmentDto.Builder()
                .id(id)
                .latitude(11.0)
                .longitude(12.0)
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("26A")
                .apartmentNumber(14)
                .build();
    }

    @Test
    public void whenTryingGetResponseFromApiThenHttpResponseIsOkAndThereIsTemperaturesInside() {
        //Given
        String url  = openWeatherApiConfig.getUrl();
        String key  = openWeatherApiConfig.getKey();
        ApartmentDto apartmentDto = createApartment(11L);
        //When
        GetTemperatureResponse temperatureResponse = openWeatherApiClient.getTemperatureResponse(apartmentDto);
        System.out.println(temperatureResponse.getTemperature().get(0).getTemperatureResponse().getTemp());
        //Then
        assertEquals("200", temperatureResponse.getHttpResponse());
        assertNotNull(temperatureResponse.getTemperature().get(0).getTemperatureResponse().getTemp());
    }


}
