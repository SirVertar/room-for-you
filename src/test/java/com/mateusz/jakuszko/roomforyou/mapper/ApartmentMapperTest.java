package com.mateusz.jakuszko.roomforyou.mapper;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
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
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("26")
                .apartmentNumber(5)
                .latitude(123L)
                .longitude(321L)
                .reservations(Collections.singletonList(Reservation.builder().id(11L).build()))
                .customer(Customer.builder().id(12L).build())
                .build();
    }

    private ApartmentDto createApartmentDto() {
        return ApartmentDto.builder()
                .id(1L)
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("26")
                .apartmentNumber(5)
                .latitude(123L)
                .longitude(321L)
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
        assertEquals(expectedApartmentDto.getLatitude(), apartmentDto.getLatitude());
        assertEquals(expectedApartmentDto.getLongitude(), apartmentDto.getLongitude());
        assertEquals(expectedApartmentDto.getApartmentNumber(), apartmentDto.getApartmentNumber());
        assertEquals(expectedApartmentDto.getCity(), apartmentDto.getCity());
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
                Customer.builder().id(12L).build());
        //Then
        assertEquals(expectedApartment.getId(), apartment.getId());
        assertEquals(expectedApartment.getCustomer().getId(), apartment.getCustomer().getId());
        assertEquals(expectedApartment.getLatitude(), apartment.getLatitude());
        assertEquals(expectedApartment.getLongitude(), apartment.getLongitude());
        assertEquals(expectedApartment.getApartmentNumber(), apartment.getApartmentNumber());
        assertEquals(expectedApartment.getStreetNumber(), apartment.getStreetNumber());
        assertEquals(expectedApartment.getCity(), apartment.getCity());
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
        assertEquals(123L, apartmentDtos.get(0).getLatitude().longValue());
        assertEquals(321L, apartmentDtos.get(0).getLongitude().longValue());
        assertEquals(12L, apartmentDtos.get(0).getUserId().longValue());
        assertEquals(11L, apartmentDtos.get(0).getReservationsIds().get(0).longValue());
        assertEquals("Terespol", apartmentDtos.get(0).getCity());
        assertEquals("Kraszewskiego", apartmentDtos.get(0).getStreet());
        assertEquals("26", apartmentDtos.get(0).getStreetNumber());
        assertEquals(5 , apartmentDtos.get(0).getApartmentNumber().longValue());
    }
}
