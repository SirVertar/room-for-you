package com.mateusz.jakuszko.roomforyou.mapper;

import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationMapperTest {

    @Autowired
    ReservationMapper reservationMapper;

    private Reservation createReservation() {
        return Reservation.builder()
                .id(1L)
                .customer(Customer.builder().id(2L).build())
                .startDate(LocalDate.of(2020, 10, 20))
                .endDate(LocalDate.of(2020, 11, 20))
                .apartment(Apartment.builder().id(3L).build())
                .build();
    }

    private ReservationDto createReservationDto() {
        return ReservationDto.builder()
                .id(1L)
                .userId(2L)
                .startDate(LocalDate.of(2020, 10, 20))
                .endDate(LocalDate.of(2020, 11, 20))
                .apartmentId(3L)
                .build();
    }

    @Test
    public void whenMapReservationToReservationDtoShouldReturnExactReservationDtoObject() {
        //Given
        Reservation reservation = createReservation();
        ReservationDto expectedReservationDto = createReservationDto();
        //When
        ReservationDto reservationDto = reservationMapper
                .mapToReservationDto(reservation);
        //Then
        assertEquals(expectedReservationDto.getId(), reservationDto.getId());
        assertEquals(expectedReservationDto.getUserId(), reservationDto.getUserId());
        assertEquals(expectedReservationDto.getApartmentId(), reservationDto.getApartmentId());
        assertEquals(expectedReservationDto.getEndDate(), reservationDto.getEndDate());
        assertEquals(expectedReservationDto.getStartDate(), reservationDto.getStartDate());
    }

    @Test
    public void whenMapReservationDtoToReservationShouldReturnExactReservationObject() {
        //Given
        ReservationDto reservationDto = createReservationDto();
        Reservation expectedReservation = createReservation();
        //When
        Reservation reservation = reservationMapper
                .mapToReservation(reservationDto, Apartment.builder().id(3L).build(), Customer.builder().id(2L).build());
        //Then
        assertEquals(expectedReservation.getId(), reservation.getId());
        assertEquals(expectedReservation.getCustomer().getId(), reservation.getCustomer().getId());
        assertEquals(expectedReservation.getApartment().getId(), reservation.getApartment().getId());
        assertEquals(expectedReservation.getEndDate(), reservation.getEndDate());
        assertEquals(expectedReservation.getStartDate(), reservation.getStartDate());
    }

    @Test
    public void whenMapReservationsToReservationDtosShouldReturnExactListOfReservationDto() {
        //Given
        List<Reservation> reservations = Arrays.asList(createReservation());
        List<ReservationDto> expectedReservationDtos = Arrays.asList(createReservationDto());
        //When
        List<ReservationDto> reservationDtos = reservationMapper.mapToReservationDtos(reservations);
        //Then
        assertEquals(expectedReservationDtos.size(), reservationDtos.size());
        assertEquals(expectedReservationDtos.get(0).getId(), reservationDtos.get(0).getId());
        assertEquals(expectedReservationDtos.get(0).getStartDate(), reservationDtos.get(0).getStartDate());
        assertEquals(expectedReservationDtos.get(0).getEndDate(), reservationDtos.get(0).getEndDate());
        assertEquals(expectedReservationDtos.get(0).getUserId(), reservationDtos.get(0).getUserId());
        assertEquals(expectedReservationDtos.get(0).getApartmentId(), reservationDtos.get(0).getApartmentId());
    }
}
