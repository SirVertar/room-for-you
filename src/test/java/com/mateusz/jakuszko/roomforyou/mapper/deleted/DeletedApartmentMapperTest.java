package com.mateusz.jakuszko.roomforyou.mapper.deleted;

import com.mateusz.jakuszko.roomforyou.dto.deleted.DeletedApartmentDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedApartment;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedReservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DeletedApartmentMapperTest {

    @Autowired
    private DeletedApartmentMapper apartmentMapper;

    private Apartment createApartment(Long id, Long customerId, Long reservationId) {
        return Apartment.builder()
                .id(id)
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("26")
                .apartmentNumber(5)
                .latitude(123.0)
                .longitude(321.0)
                .reservations(Collections.singletonList(Reservation.builder().id(reservationId).build()))
                .customer(Customer.builder().id(customerId).build())
                .build();
    }

    private DeletedApartment createDeletedApartment(Long apartmentId, Long customerId, Long reservationId) {
        return DeletedApartment.builder()
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("26")
                .apartmentNumber(5)
                .latitude(123.0)
                .longitude(321.0)
                .reservations(Collections.singletonList(DeletedReservation.builder().previousReservationId(reservationId).build()))
                .previousCustomerId(customerId)
                .previousApartmentId(apartmentId)
                .build();
    }

    @Test
    public void shouldMapApartmentToDeletedApartment() {
        //Given
        Apartment apartment = createApartment(2L, 12L, 11L);
        //When
        List<DeletedReservation> deletedReservations = Collections
                .singletonList(DeletedReservation.builder().previousReservationId(11L).build());
        DeletedApartment deletedApartment = apartmentMapper.mapToDeletedApartment(apartment, deletedReservations);
        //Then
        assertEquals(2L, deletedApartment.getPreviousApartmentId().longValue());
        assertEquals(12L, deletedApartment.getPreviousCustomerId().longValue());
        assertEquals(11L, deletedApartment.getReservations().get(0).getPreviousReservationId().longValue());
        assertEquals("Terespol", deletedApartment.getCity());
        assertEquals("Kraszewskiego", deletedApartment.getStreet());
        assertEquals("26", deletedApartment.getStreetNumber());
        assertEquals(5, deletedApartment.getApartmentNumber().intValue());
        assertEquals(123.0, deletedApartment.getLatitude(), 0);
        assertEquals(321.0, deletedApartment.getLongitude(), 0);
    }

    @Test
    public void shouldMapApartmentsToDeletedApartments() {
        //Given
        List<Apartment> apartments = new ArrayList<>();
        apartments.add(createApartment(1L, 2L, 3L));
        apartments.add(createApartment(4L, 5L, 6L));
        //When
        List<DeletedReservation> deletedReservations = new ArrayList<>();
        deletedReservations.add(DeletedReservation.builder().previousReservationId(3L).previousApartmentId(1L).build());
        deletedReservations.add(DeletedReservation.builder().previousReservationId(6L).previousApartmentId(4L).build());
        List<DeletedApartment> deletedApartments = apartmentMapper.mapToDeleteApartments(apartments, deletedReservations);
        //Then
        assertEquals(2, deletedApartments.size());
        assertTrue(deletedApartments.stream()
        .filter(apartment -> apartment.getPreviousApartmentId().equals(1L))
                .allMatch(apartment -> apartment.getCity().equals("Terespol") &&
                        apartment.getStreet().equals("Kraszewskiego") &&
                        apartment.getStreetNumber().equals("26") &&
                        apartment.getApartmentNumber().equals(5) &&
                        apartment.getLatitude().equals(123.0) &&
                        apartment.getLongitude().equals(321.0) &&
                        apartment.getPreviousCustomerId().equals(2L) &&
                        apartment.getReservations().get(0).getPreviousReservationId().equals(3L)));
        assertTrue(deletedApartments.stream()
                .filter(apartment -> apartment.getPreviousApartmentId().equals(4L))
                .allMatch(apartment -> apartment.getCity().equals("Terespol") &&
                        apartment.getStreet().equals("Kraszewskiego") &&
                        apartment.getStreetNumber().equals("26") &&
                        apartment.getApartmentNumber().equals(5) &&
                        apartment.getLatitude().equals(123.0) &&
                        apartment.getLongitude().equals(321.0) &&
                        apartment.getPreviousCustomerId().equals(5L) &&
                        apartment.getReservations().get(0).getPreviousReservationId().equals(6L)));
    }

    @Test
    public void shouldMapApartmentsWithoutReservationsToDeletedApartments() {
        //Given
        //Given
        List<Apartment> apartments = new ArrayList<>();
        apartments.add(createApartment(1L, 2L, 3L));
        apartments.add(createApartment(4L, 5L, 6L));
        //When
        List<DeletedApartment> deletedApartments = apartmentMapper.mapToDeleteApartments(apartments);
        //Then
        assertEquals(2, deletedApartments.size());
        assertTrue(deletedApartments.stream()
                .filter(apartment -> apartment.getPreviousApartmentId().equals(1L))
                .allMatch(apartment -> apartment.getCity().equals("Terespol") &&
                        apartment.getStreet().equals("Kraszewskiego") &&
                        apartment.getStreetNumber().equals("26") &&
                        apartment.getApartmentNumber().equals(5) &&
                        apartment.getLatitude().equals(123.0) &&
                        apartment.getLongitude().equals(321.0) &&
                        apartment.getPreviousCustomerId().equals(2L)));
        assertTrue(deletedApartments.stream()
                .filter(apartment -> apartment.getPreviousApartmentId().equals(4L))
                .allMatch(apartment -> apartment.getCity().equals("Terespol") &&
                        apartment.getStreet().equals("Kraszewskiego") &&
                        apartment.getStreetNumber().equals("26") &&
                        apartment.getApartmentNumber().equals(5) &&
                        apartment.getLatitude().equals(123.0) &&
                        apartment.getLongitude().equals(321.0) &&
                        apartment.getPreviousCustomerId().equals(5L)));
    }

    @Test
    public void shouldMapDeletedApartmentsToDeletedApartmentDtos() {
        //Given
        DeletedApartment deletedApartment1 = createDeletedApartment(1L, 2L, 3L);
        DeletedApartment deletedApartment2 = createDeletedApartment(4L, 5L, 6L);
        List<DeletedApartment> deletedApartments = new ArrayList<>();
        deletedApartments.add(deletedApartment1);
        deletedApartments.add(deletedApartment2);
        //When
        List<DeletedApartmentDto> deletedApartmentDtos = apartmentMapper.mapToDeletedApartmentDtos(deletedApartments);
        //Then
        assertEquals(2, deletedApartmentDtos.size());
        assertTrue(deletedApartmentDtos.stream()
                .filter(apartment -> apartment.getPreviousApartmentId().equals(1L))
                .allMatch(apartment -> apartment.getCity().equals("Terespol") &&
                        apartment.getStreet().equals("Kraszewskiego") &&
                        apartment.getStreetNumber().equals("26") &&
                        apartment.getApartmentNumber().equals(5) &&
                        apartment.getLatitude().equals(123.0) &&
                        apartment.getLongitude().equals(321.0) &&
                        apartment.getPreviousCustomerId().equals(2L) &&
                        apartment.getReservationIds().get(0).equals(3L)));
        assertTrue(deletedApartmentDtos.stream()
                .filter(apartment -> apartment.getPreviousApartmentId().equals(4L))
                .allMatch(apartment -> apartment.getCity().equals("Terespol") &&
                        apartment.getStreet().equals("Kraszewskiego") &&
                        apartment.getStreetNumber().equals("26") &&
                        apartment.getApartmentNumber().equals(5) &&
                        apartment.getLatitude().equals(123.0) &&
                        apartment.getLongitude().equals(321.0) &&
                        apartment.getPreviousCustomerId().equals(5L) &&
                        apartment.getReservationIds().get(0).equals(6L)));
    }
}
