package com.mateusz.jakuszko.roomforyou.mapper.deleted;

import com.mateusz.jakuszko.roomforyou.dto.deleted.DeletedReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedReservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DeletedReservationMapperTest {

    @Autowired
    private DeletedReservationMapper reservationMapper;

    private Reservation createReservation() {
        return Reservation.builder()
                .id(1L)
                .customer(Customer.builder().id(2L).build())
                .startDate(LocalDate.of(2020, 10, 20))
                .endDate(LocalDate.of(2020, 11, 20))
                .apartment(Apartment.builder().id(3L).build())
                .build();
    }

    private DeletedReservation createDeletedReservation() {
        return DeletedReservation.builder()
                .id(33L)
                .previousReservationId(1L)
                .previousCustomerId(2L)
                .previousApartmentId(3L)
                .startDate(LocalDate.of(2020, 10, 20))
                .endDate(LocalDate.of(2020, 11, 20))
                .build();
    }

    @Test
    public void shouldMapReservationToDeletedReservation() {
        //Given
        Reservation reservation = createReservation();
        //When
        DeletedReservation deletedReservation = reservationMapper.mapToDeletedReservation(reservation);
        //Then
        assertEquals(reservation.getId(), deletedReservation.getPreviousReservationId());
        assertEquals(reservation.getApartment().getId(), deletedReservation.getPreviousApartmentId());
        assertEquals(reservation.getCustomer().getId(), deletedReservation.getPreviousCustomerId());
        assertEquals(reservation.getEndDate(), deletedReservation.getEndDate());
        assertEquals(reservation.getStartDate(), deletedReservation.getStartDate());
    }

    @Test
    public void shouldMapReservationsToDeletedReservations() {
        //Given
        List<Reservation> reservations = Collections.singletonList(createReservation());
        //When
        List<DeletedReservation> deletedReservations = reservationMapper.mapToDeletedReservations(reservations);
        //Then
        assertEquals(reservations.get(0).getId(), deletedReservations.get(0).getPreviousReservationId());
        assertEquals(reservations.get(0).getApartment().getId(), deletedReservations.get(0).getPreviousApartmentId());
        assertEquals(reservations.get(0).getCustomer().getId(), deletedReservations.get(0).getPreviousCustomerId());
        assertEquals(reservations.get(0).getEndDate(), deletedReservations.get(0).getEndDate());
        assertEquals(reservations.get(0).getStartDate(), deletedReservations.get(0).getStartDate());
    }

    @Test
    public void shouldMapDeletedReservationsToDeletedReservationDtos() {
        //Given
        List<DeletedReservation> deletedReservations = Collections.singletonList(createDeletedReservation());
        //When
        List<DeletedReservationDto> deletedReservationDtos = reservationMapper
                .mapToDeletedReservationDtos(deletedReservations);
        //Then
        assertEquals(1, deletedReservationDtos.size());
        assertEquals(deletedReservations.get(0).getPreviousCustomerId(), deletedReservationDtos.get(0).getPreviousCustomerId());
        assertEquals(deletedReservations.get(0).getPreviousApartmentId(), deletedReservationDtos.get(0).getPreviousApartmentId());
        assertEquals(deletedReservations.get(0).getPreviousReservationId(), deletedReservationDtos.get(0).getPreviousReservationId());
        assertEquals(deletedReservations.get(0).getId(), deletedReservationDtos.get(0).getId());
        assertEquals(deletedReservations.get(0).getStartDate(), deletedReservationDtos.get(0).getStartDate());
        assertEquals(deletedReservations.get(0).getEndDate(), deletedReservationDtos.get(0).getEndDate());
    }
}
