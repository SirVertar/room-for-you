package com.mateusz.jakuszko.roomforyou.service.deleted;

import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedReservation;
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
public class DeletedReservationDbServiceTest {
    @Autowired
    private DeletedReservationDbService reservationDbService;

    private DeletedReservation createReservation() {
        return DeletedReservation.builder()
                .endDate(LocalDate.of(2020, 5, 6))
                .startDate(LocalDate.of(2020, 4, 5))
                .previousReservationId(111L)
                .build();
    }

    @Test
    public void whenSaveReservationShouldBeAbleToGetThisReservationFromDb() {
        //Given
        DeletedReservation reservation = createReservation();
        //When
        reservationDbService.save(reservation);
        Long reservationId = reservation.getId();
        Optional<DeletedReservation> savedReservation = reservationDbService.gerReservation(reservationId);
        //Then
        assertTrue(savedReservation.isPresent());
        assertEquals(LocalDate.of(2020, 5, 6), savedReservation.get().getEndDate());
        assertEquals(LocalDate.of(2020, 4, 5), savedReservation.get().getStartDate());
        assertEquals(111L, savedReservation.get().getPreviousReservationId().longValue());
    }

    @Test
    public void whenGetReservationsFromDbShouldReturnListOfAllReservations() {
        //Given
        DeletedReservation reservation1 = createReservation();
        DeletedReservation reservation2 = createReservation();
        //When
        reservationDbService.save(reservation1);
        reservationDbService.save(reservation2);
        List<DeletedReservation> reservations = reservationDbService.getReservations();
        //Then
        assertEquals(2, reservations.size());
        assertTrue(reservations.stream()
                .allMatch(reservation -> reservation.getId() != null &&
                        reservation.getEndDate().equals(LocalDate.of(2020, 5, 6)) &&
                        reservation.getStartDate().equals(LocalDate.of(2020, 4, 5)) &&
                        reservation.getPreviousReservationId().equals(111L)));

    }

    @Test
    public void whenUpdateReservationShouldReturnUpdatedReservationFromDb() {
        //Given
        DeletedReservation reservation = createReservation();
        //When
        reservationDbService.save(reservation);
        Long reservationId = reservation.getId();
        DeletedReservation savedReservation = reservationDbService
                .gerReservation(reservationId).orElseThrow(NotFoundException::new);
        savedReservation.setEndDate(LocalDate.of(2020, 7, 21));
        reservationDbService.update(savedReservation);
        Optional<DeletedReservation> updatedReservation = reservationDbService.gerReservation(reservationId);
        //Then
        assertTrue(updatedReservation.isPresent());
        assertEquals(LocalDate.of(2020, 7, 21), updatedReservation.get().getEndDate());
        assertEquals(LocalDate.of(2020, 4, 5), updatedReservation.get().getStartDate());
        assertEquals(111L, updatedReservation.get().getPreviousReservationId().longValue());
    }

    @Test
    public void whenDeleteReservationShouldNotBeAbleToFindItInDb() {
        //Given
        DeletedReservation reservation = createReservation();
        //When
        reservationDbService.save(reservation);
        Long reservationId = reservation.getId();
        reservationDbService.delete(reservationId);
        Optional<DeletedReservation> deletedReservation = reservationDbService.gerReservation(reservationId);
        //Then
        assertFalse(deletedReservation.isPresent());
    }
}
