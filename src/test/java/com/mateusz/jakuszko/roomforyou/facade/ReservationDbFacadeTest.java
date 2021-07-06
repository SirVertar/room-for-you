package com.mateusz.jakuszko.roomforyou.facade;

import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService;
import com.mateusz.jakuszko.roomforyou.service.CustomerDbService;
import com.mateusz.jakuszko.roomforyou.service.ReservationDbService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationDbFacadeTest {

    @Autowired
    private CustomerDbService customerDbService;
    @Autowired
    private ApartmentDbService apartmentDbService;
    @Autowired
    private ReservationDbService reservationDbService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ReservationDbFacade reservationDbFacade;

    private List<Long> prepareAndSaveDataIntoDbAndReturnDataIds() {
        Customer customer = Customer.builder()
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .role("Admin")
                .email("mateusz.jakuszko@gmail.com")
                .build();

        Apartment apartment = Apartment.builder()
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("26")
                .apartmentNumber(5)
                .latitude(123.0)
                .longitude(321.0)
                .customer(customer)
                .build();
        Reservation reservation = Reservation.builder()
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(4))
                .apartment(apartment)
                .customer(customer)
                .build();

        List<Apartment> apartments = new ArrayList<>();
        apartments.add(apartment);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        apartment.setReservations(reservations);

        customer.setApartments(apartments);
        customer.setReservations(reservations);

        apartmentDbService.save(apartment);
        reservationDbService.save(reservation);
        customerDbService.save(customer, passwordEncoder);

        List<Long> ids = new ArrayList<>();
        ids.add(customer.getId());
        ids.add(apartment.getId());
        ids.add(reservation.getId());
        return ids;
    }

    @Test
    public void whenGetReservationShouldReturnCorrectReservationFromDb() {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        ReservationDto reservationDto = reservationDbFacade.getReservation(reservationId);
        //Then
        assertEquals(reservationId, reservationDto.getId());
        assertEquals(LocalDate.now().plusDays(4), reservationDto.getEndDate());
        assertEquals(LocalDate.now().plusDays(1), reservationDto.getStartDate());
        assertEquals(userId, reservationDto.getCustomerId());
        assertEquals(apartmentId, reservationDto.getApartmentId());
    }

    @Test
    public void whenGetReservationsShouldReturnListOfAllReservationsFromDb() {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        List<ReservationDto> reservationDtos = reservationDbFacade.getReservations();
        //Then
        assertEquals(1, reservationDtos.size());
        assertEquals(reservationId, reservationDtos.get(0).getId());
        assertEquals(LocalDate.now().plusDays(4), reservationDtos.get(0).getEndDate());
        assertEquals(LocalDate.now().plusDays(1), reservationDtos.get(0).getStartDate());
        assertEquals(userId, reservationDtos.get(0).getCustomerId());
        assertEquals(apartmentId, reservationDtos.get(0).getApartmentId());
    }

    @Test
    public void whenCreateReservationShouldBeAbleToGetCreatedReservationFromDb() {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        ReservationDto reservationDto = reservationDbFacade.getReservation(reservationId);
        reservationDto.setId(null);
        reservationDto.setStartDate(LocalDate.now().plusDays(25));
        reservationDto.setEndDate(LocalDate.now().plusDays(52));
        //When
        reservationDbFacade.createReservation(reservationDto);
        List<Reservation> reservations = reservationDbService.getReservationsByUserId(userId);
        List<Reservation> reservationsInApartment = apartmentDbService.getApartment(apartmentId)
                .orElseThrow(NotFoundException::new).getReservations();
        //Then
        assertEquals(2, reservations.size());
        assertEquals(2, reservationsInApartment.size());
        assertTrue(reservations.stream()
                .anyMatch(reservation -> reservation.getEndDate().equals(LocalDate.now().plusDays(52)))
        );
    }

    @Test
    public void whenUpdateReservationShouldBeAbleToGetFromDbUpdatedReservationWithData() {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        ReservationDto reservationDto = reservationDbFacade.getReservation(reservationId);
        reservationDto.setEndDate(LocalDate.now().plusDays(22));
        //When
        reservationDbFacade.updateReservation(reservationDto);
        List<Reservation> reservationsInApartment = apartmentDbService.getApartment(apartmentId)
                .orElseThrow(NotFoundException::new).getReservations();
        List<Reservation> reservations = reservationDbService.getReservationsByUserId(userId);
        Optional<Reservation> reservation = reservationDbService.getReservation(reservationId);
        //Then
        assertTrue(reservation.isPresent());
        assertEquals(1, reservations.size());
        assertEquals(1, reservationsInApartment.size());
        assertEquals(reservationId, reservationDto.getId());
        assertEquals(LocalDate.now().plusDays(22), reservationDto.getEndDate());
        assertEquals(LocalDate.now().plusDays(1), reservationDto.getStartDate());
        assertEquals(userId, reservationDto.getCustomerId());
        assertEquals(apartmentId, reservationDto.getApartmentId());
    }

    @Test
    public void whenDeleteReservationShouldNotFindDeletedReservationInDb() {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        reservationDbFacade.deleteReservation(reservationId);
        Optional<Reservation> deletedReservation = reservationDbService.getReservation(reservationId);
        Optional<Apartment> apartment = apartmentDbService.getApartment(apartmentId);
        Optional<Customer> user = customerDbService.getUser(userId);
        //Then
        assertFalse(deletedReservation.isPresent());
        assertTrue(apartment.isPresent());
        assertTrue(user.isPresent());
        assertEquals(0, apartment.get().getReservations().size());
        assertEquals(0, user.get().getReservations().size());
    }
}
