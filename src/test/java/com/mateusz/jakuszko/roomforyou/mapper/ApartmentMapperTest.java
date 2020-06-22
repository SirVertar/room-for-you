package com.mateusz.jakuszko.roomforyou.mapper;

import com.mateusz.jakuszko.roomforyou.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApartmentMapperTest {

    @Autowired
    ApartmentMapper apartmentMapper;

    private Apartment createApartment() {
        return Apartment.builder()
                .id(1L)
                .street("Kraszewskiego")
                .streetNumber(26)
                .apartmentNumber(5)
                .xCoordinate(123L)
                .yCoordinate(321L)
                .reservations(Collections.singletonList(Reservation.builder().id(11L).build()))
                .user(User.builder().id(12L).build())
                .build();
    }

    private ApartmentDto createApartmentDto() {
        return ApartmentDto.builder()
                .id(1L)
                .street("Kraszewskiego")
                .streetNumber(26)
                .apartmentNumber(5)
                .xCoordinate(123L)
                .yCoordinate(321L)
                .userId(12L)
                .reservationsIds(Collections.singletonList(11L))
                .build();
    }

    @Test
    public void mapToApartmentDtoTest() {
        //Given
        Apartment apartment = createApartment();
        //When
        ApartmentDto expectedApartmentDto = createApartmentDto();
        ApartmentDto apartmentDto = apartmentMapper.mapToApartmentDto(apartment, Collections.singletonList(11L));
        //Then
        assertEquals(expectedApartmentDto.getId(), apartmentDto.getId());
        assertEquals(expectedApartmentDto.getUserId(), apartmentDto.getUserId());
        assertEquals(expectedApartmentDto.getXCoordinate(), apartmentDto.getXCoordinate());
        assertEquals(expectedApartmentDto.getYCoordinate(), apartmentDto.getYCoordinate());
        assertEquals(expectedApartmentDto.getApartmentNumber(), apartmentDto.getApartmentNumber());
        assertEquals(expectedApartmentDto.getStreetNumber(), apartmentDto.getStreetNumber());
        assertEquals(expectedApartmentDto.getStreet(), apartmentDto.getStreet());
        assertEquals(expectedApartmentDto.getReservationsIds().get(0), apartmentDto.getReservationsIds().get(0));

    }

    @Test
    public void mapToApartmentTest() {
        //Given
        ApartmentDto apartmentDto = createApartmentDto();
        //When
        Apartment expectedApartment = createApartment();
        Apartment apartment = apartmentMapper.mapToApartment(apartmentDto,
                Collections.singletonList(Reservation.builder().id(11L).build()),
                User.builder().id(12L).build());
        //Then
        assertEquals(expectedApartment.getId(), apartment.getId());
        assertEquals(expectedApartment.getUser().getId(), apartment.getUser().getId());
        assertEquals(expectedApartment.getXCoordinate(), apartment.getXCoordinate());
        assertEquals(expectedApartment.getYCoordinate(), apartment.getYCoordinate());
        assertEquals(expectedApartment.getApartmentNumber(), apartment.getApartmentNumber());
        assertEquals(expectedApartment.getStreetNumber(), apartment.getStreetNumber());
        assertEquals(expectedApartment.getStreet(), apartment.getStreet());
        assertEquals(expectedApartment.getReservations().get(0).getId(), apartment.getReservations().get(0).getId());
    }

    @Test
    public void mapToApartmentDtosTest() {
        //Given
        Apartment apartment = createApartment();
        List<Apartment> apartments = new ArrayList<>();
        apartments.add(apartment);
        //When
        List<ApartmentDto> apartmentDtos = apartmentMapper
                .mapToApartmentDtos(apartments);
        //Then
        assertEquals(1, apartmentDtos.size());
        assertEquals(1L, apartmentDtos.get(0).getId().longValue());
        assertEquals(123L, apartmentDtos.get(0).getXCoordinate().longValue());
        assertEquals(321L, apartmentDtos.get(0).getYCoordinate().longValue());
        assertEquals(12L, apartmentDtos.get(0).getUserId().longValue());
        assertEquals(11L, apartmentDtos.get(0).getReservationsIds().get(0).longValue());
        assertEquals("Kraszewskiego", apartmentDtos.get(0).getStreet());
        assertEquals(26, apartmentDtos.get(0).getStreetNumber().longValue());
        assertEquals(5 , apartmentDtos.get(0).getApartmentNumber().longValue());
    }
}
