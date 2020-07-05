package com.mateusz.jakuszko.roomforyou.opencagegeocoder.clients;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.opencagegeocoder.client.OpenCageGeocoderClient;
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
public class OpenCageGeocoderCustomerTest {

    @Autowired
    private OpenCageGeocoderClient openCageGeocoderClient;

    private ApartmentDto createApartment(Long id) {
        return ApartmentDto.builder()
                .id(id)
                .latitude(11L)
                .longitude(12L)
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("26A")
                .apartmentNumber(14)
                .build();
    }

    @Test
    public void getPositionsTest() throws IOException, ParseException {
        //Given
        ApartmentDto apartmentDto = createApartment(11L);
        //When
        Map<String, String> coordinatesMap = openCageGeocoderClient.getUrlWithApartmentDetails(apartmentDto);
        String latitude = coordinatesMap.get("latitude");
        String longitude = coordinatesMap.get("longitude");
        //Then
        assertEquals("52.0793708", latitude);
        assertEquals("23.6158891", longitude);
    }
}
