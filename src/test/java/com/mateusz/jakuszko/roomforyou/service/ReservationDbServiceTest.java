package com.mateusz.jakuszko.roomforyou.service;

import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationDbServiceTest {

    @Autowired
    private ReservationDbService reservationDbService;

    private Reservation createReservation() {
        return Reservation.builder()
                .endDate(LocalDate.of(2020, 5, 6))
                .startDate(LocalDate.of(2020, 4, 5))
                .build();
    }

    @Test
    public void saveAndGetReservationTest() {
        //Given
        Reservation reservation = createReservation();
        //When
        reservationDbService.save(reservation);
        Long reservationId = reservation.getId();
        Optional<Reservation> savedReservation = reservationDbService.gerReservation(reservationId);
        //Then
        assertTrue(savedReservation.isPresent());
        assertEquals(LocalDate.of(2020, 5, 6), savedReservation.get().getEndDate());
        assertEquals(LocalDate.of(2020, 4, 5), savedReservation.get().getStartDate());
    }

    @Test
    public void getReservationsTest() {
        //Given
        Reservation reservation1 = createReservation();
        Reservation reservation2 = createReservation();
        //When
        reservationDbService.save(reservation1);
        reservationDbService.save(reservation2);
        List<Reservation> reservations = reservationDbService.getReservations();
        //Then
        assertEquals(2, reservations.size());
        assertTrue(reservations.stream()
                .allMatch(reservation -> reservation.getId() != null &&
                        reservation.getEndDate().equals(LocalDate.of(2020, 5, 6)) &&
                        reservation.getStartDate().equals(LocalDate.of(2020, 4, 5))));

    }

    @Test
    public void updateTest() {
        //Given
        Reservation reservation = createReservation();
        //When
        reservationDbService.save(reservation);
        Long reservationId = reservation.getId();
        Reservation savedReservation = reservationDbService
                .gerReservation(reservationId).orElseThrow(NotFoundException::new);
        savedReservation.setEndDate(LocalDate.of(2020, 7, 21));
        reservationDbService.update(savedReservation);
        Optional<Reservation> updatedReservation = reservationDbService.gerReservation(reservationId);
        //Then
        assertTrue(updatedReservation.isPresent());
        assertEquals(LocalDate.of(2020, 7, 21), updatedReservation.get().getEndDate());
        assertEquals(LocalDate.of(2020, 4, 5), updatedReservation.get().getStartDate());
    }

    @Test
    public void deleteTest() {
        //Given
        Reservation reservation = createReservation();
        //When
        reservationDbService.save(reservation);
        Long reservationId = reservation.getId();
        reservationDbService.delete(reservationId);
        Optional<Reservation> deletedReservation = reservationDbService.gerReservation(reservationId);
        //Then
        assertFalse(deletedReservation.isPresent());
    }
}
