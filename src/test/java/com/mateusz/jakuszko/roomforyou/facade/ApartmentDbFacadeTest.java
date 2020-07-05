package com.mateusz.jakuszko.roomforyou.facade;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.dto.UserDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import com.mateusz.jakuszko.roomforyou.mapper.ApartmentMapper;
import com.mateusz.jakuszko.roomforyou.mapper.ReservationMapper;
import com.mateusz.jakuszko.roomforyou.mapper.UserMapper;
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService;
import com.mateusz.jakuszko.roomforyou.service.ReservationDbService;
import com.mateusz.jakuszko.roomforyou.service.UserDbService;
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
public class ApartmentDbFacadeTest {

    @Autowired
    private ApartmentDbFacade apartmentDbFacade;
    @Autowired
    private UserDbFacade userDbFacade;
    @Autowired
    private UserDbService userDbService;
    @Autowired
    private ApartmentDbService apartmentDbService;
    @Autowired
    private ReservationDbService reservationDbService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ApartmentMapper apartmentMapper;
    @Autowired
    private ReservationMapper reservationMapper;

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
                .latitude(123L)
                .longitude(321L)
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

        customer.setApartments(apartments);
        customer.setReservations(reservations);

        apartmentDbService.save(apartment);
        reservationDbService.save(reservation);
        userDbService.save(customer, passwordEncoder);

        List<Long> ids = new ArrayList<>();
        ids.add(customer.getId());
        ids.add(apartment.getId());
        ids.add(reservation.getId());
        return ids;
    }

    @Test
    public void createApartment() {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        List<Long> reservationsIds = new ArrayList<>();
        UserDto userDto = UserDto.builder()
                .id(userId)
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .role("Admin")
                .email("mateusz.jakuszko@gmail.com")
                .build();
        ApartmentDto apartmentDto = ApartmentDto.builder()
                .city("Terespol")
                .street("New Street")
                .streetNumber("26")
                .apartmentNumber(5)
                .latitude(123L)
                .longitude(321L)
                .userId(userId)
                .reservationsIds(reservationsIds)
                .build();

        List<ReservationDto> reservations = new ArrayList<>();
        List<ApartmentDto> apartments = new ArrayList<>();
        apartments.add(apartmentDto);

        userDto.setApartments(apartments);
        userDto.setReservations(reservations);

        //When
        apartmentDbFacade.createApartment(apartmentDto);
        Optional<Apartment> apartment = userDbService.getByUsername("matanos")
                .orElseThrow(NotFoundException::new).getApartments().stream()
                .filter(e -> e.getStreet().equals("New Street"))
                .findFirst();
        //Then
        assertTrue(apartment.isPresent());
        assertEquals(123L, apartment.get().getLatitude().longValue());
        assertEquals(321L, apartment.get().getLongitude().longValue());
        assertEquals("Terespol", apartment.get().getCity());
        assertEquals("New Street", apartment.get().getStreet());
        assertEquals("26", apartment.get().getStreetNumber());
        assertEquals(5, apartment.get().getApartmentNumber().intValue());


    }

    @Test
    public void getApartmentTest() {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        ApartmentDto apartment = apartmentDbFacade.getApartment(apartmentId);
        //Then
        assertEquals(apartmentId, apartment.getId());
        assertEquals(123L, apartment.getLatitude().longValue());
        assertEquals(321L, apartment.getLongitude().longValue());
        assertEquals("Terespol", apartment.getCity());
        assertEquals("Kraszewskiego", apartment.getStreet());
        assertEquals("26", apartment.getStreetNumber());
        assertEquals(5, apartment.getApartmentNumber().intValue());
        assertEquals(userId, apartment.getUserId());
        assertTrue(apartment.getReservationsIds().stream()
        .allMatch(e -> e.equals(reservationId)));
    }

    @Test
    public void getApartmentsTest() {
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
        assertEquals(123L, apartments.get(0).getLatitude().longValue());
        assertEquals(321L, apartments.get(0).getLongitude().longValue());
        assertEquals("Terespol", apartments.get(0).getCity());
        assertEquals("Kraszewskiego", apartments.get(0).getStreet());
        assertEquals("26", apartments.get(0).getStreetNumber());
        assertEquals(5, apartments.get(0).getApartmentNumber().intValue());
        assertEquals(userId, apartments.get(0).getUserId());
        assertTrue(apartments.get(0).getReservationsIds().stream()
                .allMatch(e -> e.equals(reservationId)));
    }

    @Test
    public void createApartmentTest() {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        ApartmentDto apartment = apartmentDbFacade.getApartment(apartmentId);
        apartment.setStreet("NewStreet");
        apartment.setId(null);
        apartment.setReservationsIds(null);
        apartmentDbFacade.createApartment(apartment);
        ApartmentDto newApartment = apartmentDbFacade.getApartments().stream()
                .filter(e -> !e.getId().equals(apartmentId))
                .findAny().orElseThrow(NotFoundException::new);
        Customer customer = userDbService.getUser(userId).orElseThrow(NotFoundException::new);
        //Then
        assertEquals(123L, newApartment.getLatitude().longValue());
        assertEquals(321L, newApartment.getLongitude().longValue());
        assertEquals("Terespol", newApartment.getCity());
        assertEquals("NewStreet", newApartment.getStreet());
        assertEquals("26", newApartment.getStreetNumber());
        assertEquals(5, newApartment.getApartmentNumber().intValue());
        assertEquals(userId, newApartment.getUserId());
        assertEquals(0, newApartment.getReservationsIds().size());
        assertTrue(newApartment.getReservationsIds().stream()
                .allMatch(e -> e.equals(reservationId)));
        assertTrue(customer.getApartments().stream()
        .anyMatch(e -> e.getStreet().equals("NewStreet")));
    }

    @Test
    public void updateApartmentTest() {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        ApartmentDto apartment = apartmentDbFacade.getApartment(apartmentId);
        apartment.setStreet("UpdatedStreet");
        apartmentDbFacade.updateApartment(apartment);
        ApartmentDto updatedApartment = apartmentDbFacade.getApartment(apartmentId);
        Customer customer = userDbService.getUser(userId).orElseThrow(NotFoundException::new);
        //Then
        assertEquals(123L, updatedApartment.getLatitude().longValue());
        assertEquals(321L, updatedApartment.getLongitude().longValue());
        assertEquals("Terespol", updatedApartment.getCity());
        assertEquals("UpdatedStreet", updatedApartment.getStreet());
        assertEquals("26", updatedApartment.getStreetNumber());
        assertEquals(5, updatedApartment.getApartmentNumber().intValue());
        assertEquals("UpdatedStreet", customer.getApartments().get(0).getStreet());
    }

    @Test()
    public void deleteApartmentTest() {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        apartmentDbFacade.deleteApartment(apartmentId);
        Optional<Apartment> deletedApartment = apartmentDbService.getApartment(apartmentId);
        Optional<Customer> user = userDbService.getUser(userId);
        Optional<Reservation> reservation = reservationDbService.gerReservation(reservationId);
        //Then
        assertFalse(deletedApartment.isPresent());
        assertTrue(user.isPresent());
        assertFalse(reservation.isPresent());
        assertEquals(0, user.get().getReservations().size());
        assertEquals(0, user.get().getApartments().size());
    }
}
