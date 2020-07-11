package com.mateusz.jakuszko.roomforyou.validator;

import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService;
import com.mateusz.jakuszko.roomforyou.service.ReservationDbService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class ReservationValidatorTest {

    @Autowired
    private ReservationValidator reservationValidator;
    @Autowired
    private ReservationDbService reservationDbService;
    @Autowired
    private ApartmentDbService apartmentDbService;

    private void prepareData(LocalDate startDate, LocalDate endDate) {
        Apartment apartment = Apartment.builder()
                .latitude(11.0)
                .longitude(12.0)
                .street("WallStreet")
                .streetNumber("13")
                .apartmentNumber(14)
                .build();

        Reservation reservation = Reservation.builder()
                .endDate(endDate)
                .startDate(startDate)
                .apartment(apartment)
                .build();
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        apartment.setReservations(reservations);

        apartmentDbService.save(apartment);
        reservationDbService.save(reservation);
    }

    @Test
    public void whenMakeReservationWhenItIsNotPossibleThenReturnFalseAndWhenItIsPossibleThanReturnTrue() {
        //Given
        prepareData(LocalDate.of(2020, 8, 12), LocalDate.of(2020, 8, 20));
        Apartment apartment = apartmentDbService.getApartments().get(0);
        ReservationDto reservation1 = ReservationDto.builder()
                .apartmentId(apartment.getId())
                .startDate(LocalDate.of(2020, 8, 12))
                .endDate(LocalDate.of(2020, 8, 20)).build();
        ReservationDto reservation2 = ReservationDto.builder()
                .apartmentId(apartment.getId())
                .startDate(LocalDate.of(2020, 8, 2))
                .endDate(LocalDate.of(2020, 8, 11)).build();
        ReservationDto reservation3 = ReservationDto.builder()
                .apartmentId(apartment.getId())
                .startDate(LocalDate.of(2020, 8, 2))
                .endDate(LocalDate.of(2020, 8, 12)).build();
        ReservationDto reservation4 = ReservationDto.builder()
                .apartmentId(apartment.getId())
                .startDate(LocalDate.of(2020, 8, 20))
                .endDate(LocalDate.of(2020, 9, 12)).build();
        ReservationDto reservation5 = ReservationDto.builder()
                .apartmentId(apartment.getId())
                .startDate(LocalDate.of(2020, 8, 21))
                .endDate(LocalDate.of(2020, 9, 12)).build();
        ReservationDto reservation6 = ReservationDto.builder()
                .apartmentId(apartment.getId())
                .startDate(LocalDate.of(2020, 8, 15))
                .endDate(LocalDate.of(2020, 9, 12)).build();
        ReservationDto reservation7 = ReservationDto.builder()
                .apartmentId(apartment.getId())
                .startDate(LocalDate.of(2020, 8, 8))
                .endDate(LocalDate.of(2020, 8, 15)).build();

        //When
        boolean isItPossibleToMakeReservation1 = reservationValidator.checkIsTherePossibilityToMakeReservation(reservation1);
        boolean isItPossibleToMakeReservation2 = reservationValidator.checkIsTherePossibilityToMakeReservation(reservation2);
        boolean isItPossibleToMakeReservation3 = reservationValidator.checkIsTherePossibilityToMakeReservation(reservation3);
        boolean isItPossibleToMakeReservation4 = reservationValidator.checkIsTherePossibilityToMakeReservation(reservation4);
        boolean isItPossibleToMakeReservation5 = reservationValidator.checkIsTherePossibilityToMakeReservation(reservation5);
        boolean isItPossibleToMakeReservation6 = reservationValidator.checkIsTherePossibilityToMakeReservation(reservation6);
        boolean isItPossibleToMakeReservation7 = reservationValidator.checkIsTherePossibilityToMakeReservation(reservation7);
        //Then
        assertFalse(isItPossibleToMakeReservation1);
        assertTrue(isItPossibleToMakeReservation2);
        assertTrue(isItPossibleToMakeReservation3);
        assertTrue(isItPossibleToMakeReservation4);
        assertTrue(isItPossibleToMakeReservation5);
        assertFalse(isItPossibleToMakeReservation6);
        assertFalse(isItPossibleToMakeReservation7);
    }
}
