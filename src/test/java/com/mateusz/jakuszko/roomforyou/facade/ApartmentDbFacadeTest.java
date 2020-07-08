package com.mateusz.jakuszko.roomforyou.facade;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import com.mateusz.jakuszko.roomforyou.mapper.ApartmentMapper;
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService;
import com.mateusz.jakuszko.roomforyou.service.CustomerDbService;
import com.mateusz.jakuszko.roomforyou.service.ReservationDbService;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

//TODO
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApartmentDbFacadeTest {

    @Autowired
    private ApartmentDbFacade apartmentDbFacade;
    @Autowired
    private CustomerDbService customerDbService;
    @Autowired
    private ApartmentDbService apartmentDbService;
    @Autowired
    private ReservationDbService reservationDbService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerDbFacade customerDbFacade;
    @Autowired
    private ApartmentMapper apartmentMapper;

    private List<Long> prepareAndSaveDataIntoDbAndReturnDataIds() throws IOException, ParseException {
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
                .streetNumber("26A")
                .latitude(11.0)
                .longitude(12.0)
                .apartmentNumber(5)
                .customer(customer)
                .build();
        Reservation reservation = Reservation.builder()
                .startDate(LocalDate.of(2020, 1, 12))
                .endDate(LocalDate.of(2020, 3, 14))
                .apartment(apartment)
                .customer(customer)
                .build();

        List<Apartment> apartments = new ArrayList<>();
        apartments.add(apartment);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        apartment.setReservations(reservations);
        customerDbService.save(customer, passwordEncoder);
        customer.setApartments(apartments);
        customer.setReservations(reservations);
        apartment.setCustomer(customer);
        apartmentDbService.save(apartment);
        reservationDbService.save(reservation);


        List<Long> ids = new ArrayList<>();
        ids.add(customer.getId());
        ids.add(apartment.getId());
        ids.add(reservation.getId());
        return ids;
    }

    @Test
    public void createApartment() throws IOException, ParseException {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        List<Long> reservationsIds = new ArrayList<>();
        Customer customer = Customer.builder()
                .name("Mateusz")
                .surname("Jakuszko")
                .username("Mekon")
                .password("abc123")
                .role("Admin")
                .email("mateusz.jakuszko@gmail.com")
                .build();
        customerDbService.save(customer, passwordEncoder);
        ApartmentDto apartmentDto = ApartmentDto.builder()
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("26A")
                .apartmentNumber(5)
                .userId(customer.getId())
                .reservationsIds(reservationsIds)
                .build();

        List<ReservationDto> reservations = new ArrayList<>();
        List<ApartmentDto> apartments = new ArrayList<>();
        apartments.add(apartmentDto);

        //When
        apartmentDbFacade.createApartment(apartmentDto);
        Optional<Apartment> apartment = customerDbService.getByUsername("Mekon")
                .orElseThrow(NotFoundException::new).getApartments().stream()
                .filter(e -> e.getStreet().equals("Kraszewskiego"))
                .findFirst();
        //Then
        assertTrue(apartment.isPresent());
        assertEquals(52.0793708, apartment.get().getLatitude(), 0);
        assertEquals(23.6158891, apartment.get().getLongitude(), 0);
        assertEquals("Terespol", apartment.get().getCity());
        assertEquals("Kraszewskiego", apartment.get().getStreet());
        assertEquals("26A", apartment.get().getStreetNumber());
        assertEquals(5, apartment.get().getApartmentNumber().intValue());


    }

    @Test
    public void getApartmentTest() throws IOException, ParseException {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        ApartmentDto apartment = apartmentDbFacade.getApartment(apartmentId);
        //Then
        assertEquals(apartmentId, apartment.getId());
        assertEquals(11.0, apartment.getLatitude(), 0);
        assertEquals(12.0, apartment.getLongitude(), 0);
        assertEquals("Terespol", apartment.getCity());
        assertEquals("Kraszewskiego", apartment.getStreet());
        assertEquals("26A", apartment.getStreetNumber());
        assertEquals(5, apartment.getApartmentNumber().intValue());
        assertEquals(userId, apartment.getUserId());
        assertTrue(apartment.getReservationsIds().stream()
                .allMatch(e -> e.equals(reservationId)));
    }

    @Test
    public void getApartmentsTest() throws IOException, ParseException {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        List<ApartmentDto> apartments = apartmentDbFacade.getApartments();
        //Then
        assertEquals(1, apartments.size());
        assertEquals(apartmentId, apartments.get(0).getId());
        assertEquals(11.0, apartments.get(0).getLatitude(), 0);
        assertEquals(12.0, apartments.get(0).getLongitude(), 0);
        assertEquals("Terespol", apartments.get(0).getCity());
        assertEquals("Kraszewskiego", apartments.get(0).getStreet());
        assertEquals("26A", apartments.get(0).getStreetNumber());
        assertEquals(5, apartments.get(0).getApartmentNumber().intValue());
        assertEquals(userId, apartments.get(0).getUserId());
        assertTrue(apartments.get(0).getReservationsIds().stream()
                .allMatch(e -> e.equals(reservationId)));
    }

    @Test
    public void createApartmentTest() throws IOException, ParseException {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        ApartmentDto apartment = apartmentDbFacade.getApartment(apartmentId);
        apartment.setId(null);
        apartment.setReservationsIds(null);
        apartmentDbFacade.createApartment(apartment);
        ApartmentDto newApartmentDto = apartmentDbFacade.getApartments().stream()
                .filter(e -> !e.getId().equals(apartmentId))
                .findAny().orElseThrow(NotFoundException::new);
        newApartmentDto.setApartmentNumber(11);
        ApartmentDto newApartmentAfterCreate = apartmentDbFacade.createApartment(newApartmentDto);
        Customer customer = customerDbService.getUser(userId).orElseThrow(NotFoundException::new);
        //Then
        assertEquals(52.0793708, newApartmentAfterCreate.getLatitude(), 0);
        assertEquals(23.6158891, newApartmentAfterCreate.getLongitude(), 0);
        assertEquals("Terespol", newApartmentAfterCreate.getCity());
        assertEquals("Kraszewskiego", newApartmentAfterCreate.getStreet());
        assertEquals("26A", newApartmentAfterCreate.getStreetNumber());
        assertEquals(11, newApartmentAfterCreate.getApartmentNumber().intValue());
        assertEquals(userId, newApartmentAfterCreate.getUserId());
        assertEquals(0, newApartmentAfterCreate.getReservationsIds().size());
        assertTrue(newApartmentAfterCreate.getReservationsIds().stream()
                .allMatch(e -> e.equals(reservationId)));
        assertTrue(customer.getApartments().stream()
                .anyMatch(e -> e.getApartmentNumber().equals(11)));
    }

    @Test
    public void updateApartmentTest() throws IOException, ParseException {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        ApartmentDto apartment = apartmentDbFacade.getApartment(apartmentId);
        apartment.setApartmentNumber(11);
        apartmentDbFacade.updateApartment(apartment);
        ApartmentDto updatedApartment = apartmentDbFacade.getApartment(apartmentId);
        Customer customer = customerDbService.getUser(userId).orElseThrow(NotFoundException::new);
        //Then
        assertEquals(52.0793708, updatedApartment.getLatitude(), 0);
        assertEquals(23.6158891, updatedApartment.getLongitude(), 0);
        assertEquals("Terespol", updatedApartment.getCity());
        assertEquals("Kraszewskiego", updatedApartment.getStreet());
        assertEquals("26A", updatedApartment.getStreetNumber());
        assertEquals(11, updatedApartment.getApartmentNumber().intValue());
    }

    @Test()
    public void deleteApartmentTest() throws IOException, ParseException {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        apartmentDbFacade.deleteApartment(apartmentId);
        Optional<Apartment> deletedApartment = apartmentDbService.getApartment(apartmentId);
        Optional<Customer> user = customerDbService.getUser(userId);
        Optional<Reservation> reservation = reservationDbService.gerReservation(reservationId);
        //Then
        assertFalse(deletedApartment.isPresent());
        assertTrue(user.isPresent());
        assertFalse(reservation.isPresent());
        assertEquals(0, user.get().getReservations().size());
        assertEquals(0, user.get().getApartments().size());
    }
}
