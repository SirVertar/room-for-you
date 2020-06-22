package com.mateusz.jakuszko.roomforyou.facade;

import com.mateusz.jakuszko.roomforyou.domain.Apartment;
import com.mateusz.jakuszko.roomforyou.domain.Reservation;
import com.mateusz.jakuszko.roomforyou.domain.ReservationDto;
import com.mateusz.jakuszko.roomforyou.domain.User;
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
public class ReservationDbFacadeTest {

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
    @Autowired
    private ReservationDbFacade reservationDbFacade;

    private List<Long> prepareAndSaveDataIntoDbAndReturnDataIds() {
        User user = User.builder()
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .role("Admin")
                .email("mateusz.jakuszko@gmail.com")
                .build();

        Apartment apartment = Apartment.builder()
                .street("Kraszewskiego")
                .streetNumber(26)
                .apartmentNumber(5)
                .xCoordinate(123L)
                .yCoordinate(321L)
                .user(user)
                .build();
        Reservation reservation = Reservation.builder()
                .startDate(LocalDate.of(2020, 1, 12))
                .endDate(LocalDate.of(2020, 3, 14))
                .apartment(apartment)
                .user(user)
                .build();

        List<Apartment> apartments = new ArrayList<>();
        apartments.add(apartment);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        apartment.setReservations(reservations);

        user.setApartments(apartments);
        user.setReservations(reservations);

        apartmentDbService.save(apartment);
        reservationDbService.save(reservation);
        userDbService.save(user, passwordEncoder);

        List<Long> ids = new ArrayList<>();
        ids.add(user.getId());
        ids.add(apartment.getId());
        ids.add(reservation.getId());
        return ids;
    }

    @Test
    public void getReservationTest() {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        ReservationDto reservationDto = reservationDbFacade.getReservation(reservationId);
        //Then
        assertEquals(reservationId, reservationDto.getId());
        assertEquals(LocalDate.of(2020, 3, 14), reservationDto.getEndDate());
        assertEquals(LocalDate.of(2020, 1, 12), reservationDto.getStartDate());
        assertEquals(userId, reservationDto.getUserId());
        assertEquals(apartmentId, reservationDto.getApartmentDto().getId());
    }

    @Test
    public void getReservationsTest() {
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
        assertEquals(LocalDate.of(2020, 3, 14), reservationDtos.get(0).getEndDate());
        assertEquals(LocalDate.of(2020, 1, 12), reservationDtos.get(0).getStartDate());
        assertEquals(userId, reservationDtos.get(0).getUserId());
        assertEquals(apartmentId, reservationDtos.get(0).getApartmentDto().getId());
    }

    @Test
    public void createReservationTest() {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        ReservationDto reservationDto = reservationDbFacade.getReservation(reservationId);
        reservationDto.setId(null);
        reservationDto.setEndDate(LocalDate.of(1990, 5, 10));
        //When
        reservationDbFacade.createReservation(reservationDto);
        List<Reservation> reservations = reservationDbService.getReservationsByUserId(userId);
        List<Reservation> reservationsInApartment = apartmentDbService.getApartment(apartmentId)
                .orElseThrow(NotFoundException::new).getReservations();
        //Then
        assertEquals(2, reservations.size());
        assertEquals(2, reservationsInApartment.size());
        assertTrue(reservations.stream()
                .anyMatch(reservation -> reservation.getEndDate().equals(LocalDate.of(1990, 5, 10)))
        );
    }

    @Test
    public void updateReservationTest() {
        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        ReservationDto reservationDto = reservationDbFacade.getReservation(reservationId);
        reservationDto.setEndDate(LocalDate.of(1990, 5, 10));
        //When
        reservationDbFacade.updateReservation(reservationDto);
        List<Reservation> reservationsInApartment = apartmentDbService.getApartment(apartmentId)
                .orElseThrow(NotFoundException::new).getReservations();
        List<Reservation> reservations = reservationDbService.getReservationsByUserId(userId);
        Optional<Reservation> reservation = reservationDbService.gerReservation(reservationId);
        //Then
        assertTrue(reservation.isPresent());
        assertEquals(1, reservations.size());
        assertEquals(1, reservationsInApartment.size());
        assertEquals(reservationId, reservationDto.getId());
        assertEquals(LocalDate.of(1990, 5, 10), reservationDto.getEndDate());
        assertEquals(LocalDate.of(2020, 1, 12), reservationDto.getStartDate());
        assertEquals(userId, reservationDto.getUserId());
        assertEquals(apartmentId, reservationDto.getApartmentDto().getId());
    }

    @Test
    public void deleteReservationTest() {

        //Given
        List<Long> ids = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = ids.get(0);
        Long apartmentId = ids.get(1);
        Long reservationId = ids.get(2);
        //When
        reservationDbFacade.deleteReservation(reservationId);
        Optional<Reservation> deletedReservation = reservationDbService.gerReservation(reservationId);
        Optional<Apartment> apartment = apartmentDbService.getApartment(apartmentId);
        Optional<User> user = userDbService.getUser(userId);
        //Then
        assertFalse(deletedReservation.isPresent());
        assertTrue(apartment.isPresent());
        assertTrue(user.isPresent());
        assertEquals(0, apartment.get().getReservations().size());
        assertEquals(0, user.get().getReservations().size());
    }
}
