package com.mateusz.jakuszko.roomforyou.opencagegeocoderapi;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OpenCageGeocoderClientTest {

    @Autowired
    private OpenCageGeocoderClient openCageGeocoderClient;

    private ApartmentDto createApartment(Long id) {
        return ApartmentDto.builder()
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
    public void whenPutApartmentDtoThenReceiveGeolocationValues() throws IOException, ParseException {
        //Given
        ApartmentDto apartmentDto = ApartmentDto.builder()
                .id(1L)
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("26A")
                .apartmentNumber(14)
                .build();
        //When
        Map<String, String> coordinatesMap = openCageGeocoderClient.getGeometryValues(apartmentDto);
        String latitude = coordinatesMap.get("latitude");
        String longitude = coordinatesMap.get("longitude");
        //Then
        assertEquals("52.0793708", latitude);
        assertEquals("23.6158891", longitude);
    }

    @Test
    public void whenPutApartmentDtoWithAThenReceiveGeolocationValues() throws IOException, ParseException {
        //Given
        ApartmentDto apartmentDto = ApartmentDto.builder()
                .id(1L)
                .city("Poznań")
                .street("Stefana Żeromskiego")
                .streetNumber("26")
                .apartmentNumber(14)
                .build();
        //When
        Map<String, String> coordinatesMap = openCageGeocoderClient.getGeometryValues(apartmentDto);
        String latitude = coordinatesMap.get("latitude");
        String longitude = coordinatesMap.get("longitude");
        //Then
        assertEquals("52.4189675", latitude);
        assertEquals("16.8941083", longitude);
    }

}
