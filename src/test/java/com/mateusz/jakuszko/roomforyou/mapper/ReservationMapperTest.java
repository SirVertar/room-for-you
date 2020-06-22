package com.mateusz.jakuszko.roomforyou.mapper;

import com.mateusz.jakuszko.roomforyou.domain.*;
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
                .user(User.builder().id(2L).build())
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
                .apartmentDto(ApartmentDto.builder().id(3L).build())
                .build();
    }

    @Test
    public void mapToReservationDtoTest() {
        //Given
        Reservation reservation = createReservation();
        ReservationDto expectedReservationDto = createReservationDto();
        //When
        ReservationDto reservationDto = reservationMapper
                .mapToReservationDto(reservation, ApartmentDto.builder().id(3L).build());
        //Then
        assertEquals(expectedReservationDto.getId(), reservationDto.getId());
        assertEquals(expectedReservationDto.getUserId(), reservationDto.getUserId());
        assertEquals(expectedReservationDto.getApartmentDto().getId(), reservationDto.getApartmentDto().getId());
        assertEquals(expectedReservationDto.getEndDate(), reservationDto.getEndDate());
        assertEquals(expectedReservationDto.getStartDate(), reservationDto.getStartDate());

    }

    @Test
    public void mapToReservationTest() {
        //Given
        ReservationDto reservationDto = createReservationDto();
        Reservation expectedReservation = createReservation();
        //When
        Reservation reservation = reservationMapper
                .mapToReservation(reservationDto, Apartment.builder().id(3L).build(), User.builder().id(2L).build());
        //Then
        assertEquals(expectedReservation.getId(), reservation.getId());
        assertEquals(expectedReservation.getUser().getId(), reservation.getUser().getId());
        assertEquals(expectedReservation.getApartment().getId(), reservation.getApartment().getId());
        assertEquals(expectedReservation.getEndDate(), reservation.getEndDate());
        assertEquals(expectedReservation.getStartDate(), reservation.getStartDate());
    }

    @Test
    public void mapToReservationDtosTest() {
        //Given
        List<Reservation> reservations = Arrays.asList(createReservation());
        List<ReservationDto> expectedReservationDtos = Arrays.asList(createReservationDto());
        //When
        List<ReservationDto> reservationDtos = reservationMapper.mapToReservationDtos(reservations,
                Arrays.asList(ApartmentDto.builder().id(3L).reservationsIds(Arrays.asList(1L)).build()));
        //Then
        assertEquals(expectedReservationDtos.size(), reservationDtos.size());
        assertEquals(expectedReservationDtos.get(0).getId(), reservationDtos.get(0).getId());
        assertEquals(expectedReservationDtos.get(0).getStartDate(), reservationDtos.get(0).getStartDate());
        assertEquals(expectedReservationDtos.get(0).getEndDate(), reservationDtos.get(0).getEndDate());
        assertEquals(expectedReservationDtos.get(0).getUserId(), reservationDtos.get(0).getUserId());
        assertEquals(expectedReservationDtos.get(0).getApartmentDto().getId(), reservationDtos.get(0).getApartmentDto().getId());
    }
}
