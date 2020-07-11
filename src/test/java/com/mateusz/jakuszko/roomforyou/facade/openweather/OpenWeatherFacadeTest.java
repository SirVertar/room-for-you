package com.mateusz.jakuszko.roomforyou.facade.openweather;

import com.mateusz.jakuszko.roomforyou.dto.TemperatureDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OpenWeatherFacadeTest {
    @Autowired
    private OpenWeatherFacade openWeatherFacade;
    @Autowired
    private ApartmentDbService apartmentDbService;

    @Test
    public void whenGivenAnApartmentIdThenReceiveTemperaturesForThatApartmentGeolocation() {
        //Given
        Apartment apartment = Apartment.builder()
                .latitude(52.4189675)
                .longitude(16.8941083)
                .street("WallStreet")
                .streetNumber("13")
                .apartmentNumber(14)
                .build();
        apartmentDbService.save(apartment);
        Long apartmentId = apartment.getId();
        int theHighestAirTemperatureInTheHistoryOfTheWord = 60;
        int theLowestAirTemperatureInTheHistoryOfTheWord = -90;
        //When
        TemperatureDto temperatureDto = openWeatherFacade.getTemperaturesFromWeatherApiResponse(apartmentId);

        //Then
        assertNotNull(temperatureDto.getTemp());

        assertTrue(temperatureDto.getTemp().intValue() > theLowestAirTemperatureInTheHistoryOfTheWord &&
                temperatureDto.getTemp().intValue() < theHighestAirTemperatureInTheHistoryOfTheWord);
    }
}
