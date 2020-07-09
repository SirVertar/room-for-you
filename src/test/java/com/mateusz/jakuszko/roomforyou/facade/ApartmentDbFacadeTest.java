package com.mateusz.jakuszko.roomforyou.facade;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
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
    public void whenCreateApartmentShouldBeAbleToGetCreatedApartmentFromDb() throws IOException, ParseException {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        ApartmentDto apartmentDto = apartmentDbFacade.getApartment(apartmentId);
        apartmentDto.setId(null);
        apartmentDto.setReservationsIds(null);
        apartmentDbFacade.createApartment(apartmentDto);
        ApartmentDto newApartmentDto = apartmentDbFacade.getApartments().stream()
                .filter(e -> !e.getId().equals(apartmentId))
                .findAny().orElseThrow(NotFoundException::new);
        newApartmentDto.setApartmentNumber(11);
        ApartmentDto newApartmentDtoAfterCreate = apartmentDbFacade.createApartment(newApartmentDto);
        Customer customer = customerDbService.getUser(userId).orElseThrow(NotFoundException::new);
        //Then
        assertEquals(52.0793708, newApartmentDtoAfterCreate.getLatitude(), 0);
        assertEquals(23.6158891, newApartmentDtoAfterCreate.getLongitude(), 0);
        assertEquals("Terespol", newApartmentDtoAfterCreate.getCity());
        assertEquals("Kraszewskiego", newApartmentDtoAfterCreate.getStreet());
        assertEquals("26A", newApartmentDtoAfterCreate.getStreetNumber());
        assertEquals(11, newApartmentDtoAfterCreate.getApartmentNumber().intValue());
        assertEquals(userId, newApartmentDtoAfterCreate.getUserId());
        assertEquals(0, newApartmentDtoAfterCreate.getReservationsIds().size());
        assertTrue(newApartmentDtoAfterCreate.getReservationsIds().stream()
                .allMatch(e -> e.equals(reservationId)));
        assertTrue(customer.getApartments().stream()
                .anyMatch(e -> e.getApartmentNumber().equals(11)));
    }

    @Test
    public void whenGetApartmentShouldReturnCorrectApartmentFromDb() {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        ApartmentDto apartmentDto = apartmentDbFacade.getApartment(apartmentId);
        //Then
        assertEquals(apartmentId, apartmentDto.getId());
        assertEquals(11.0, apartmentDto.getLatitude(), 0);
        assertEquals(12.0, apartmentDto.getLongitude(), 0);
        assertEquals("Terespol", apartmentDto.getCity());
        assertEquals("Kraszewskiego", apartmentDto.getStreet());
        assertEquals("26A", apartmentDto.getStreetNumber());
        assertEquals(5, apartmentDto.getApartmentNumber().intValue());
        assertEquals(userId, apartmentDto.getUserId());
        assertTrue(apartmentDto.getReservationsIds().stream()
                .allMatch(e -> e.equals(reservationId)));
    }

    @Test
    public void whenGetApartmentsShouldReturnListOfAllApartmentsFromDb() throws IOException, ParseException {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        List<ApartmentDto> apartmentDtos = apartmentDbFacade.getApartments();
        //Then
        assertEquals(1, apartmentDtos.size());
        assertEquals(apartmentId, apartmentDtos.get(0).getId());
        assertEquals(11.0, apartmentDtos.get(0).getLatitude(), 0);
        assertEquals(12.0, apartmentDtos.get(0).getLongitude(), 0);
        assertEquals("Terespol", apartmentDtos.get(0).getCity());
        assertEquals("Kraszewskiego", apartmentDtos.get(0).getStreet());
        assertEquals("26A", apartmentDtos.get(0).getStreetNumber());
        assertEquals(5, apartmentDtos.get(0).getApartmentNumber().intValue());
        assertEquals(userId, apartmentDtos.get(0).getUserId());
        assertTrue(apartmentDtos.get(0).getReservationsIds().stream()
                .allMatch(e -> e.equals(reservationId)));
    }

    @Test
    public void whenUpdateApartmentShouldGetFromDbApartmentWithUpdatedData() throws IOException, ParseException {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        ApartmentDto apartmentDto = apartmentDbFacade.getApartment(apartmentId);
        apartmentDto.setApartmentNumber(11);
        apartmentDbFacade.updateApartment(apartmentDto);
        ApartmentDto updatedApartmentDto = apartmentDbFacade.getApartment(apartmentId);
        Customer customer = customerDbService.getUser(userId).orElseThrow(NotFoundException::new);
        //Then
        assertEquals(52.0793708, updatedApartmentDto.getLatitude(), 0);
        assertEquals(23.6158891, updatedApartmentDto.getLongitude(), 0);
        assertEquals("Terespol", updatedApartmentDto.getCity());
        assertEquals("Kraszewskiego", updatedApartmentDto.getStreet());
        assertEquals("26A", updatedApartmentDto.getStreetNumber());
        assertEquals(11, updatedApartmentDto.getApartmentNumber().intValue());
    }

    @Test()
    public void whenDeleteApartmentShouldNotFindDeletedApartmentInDb() throws IOException, ParseException {
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
