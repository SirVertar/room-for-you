package com.mateusz.jakuszko.roomforyou.facade;

import com.mateusz.jakuszko.roomforyou.domain.*;
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
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDbFacadeTest {

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
    public void getUserTest() {
        //Given
        Long userId = prepareAndSaveDataIntoDbAndReturnDataIds().get(0);
        //When
        UserDto userDto = userDbFacade.getUser(userId);
        //Then
        assertEquals("Mateusz", userDto.getName());
        assertEquals("Jakuszko", userDto.getSurname());
        assertEquals("matanos", userDto.getUsername());
        assertEquals("mateusz.jakuszko@gmail.com", userDto.getEmail());
        assertEquals(userId, userDto.getId());
        assertTrue(passwordEncoder.matches("abc123", userDto.getPassword()));
        assertTrue(userDto.getReservations().stream()
                .allMatch(reservationDto -> reservationDto.getStartDate().equals(LocalDate.of(2020, 1, 12)) &&
                        reservationDto.getEndDate().equals(LocalDate.of(2020, 3, 14))));
        assertTrue(userDto.getApartments().stream()
                .allMatch(apartmentDto -> apartmentDto.getXCoordinate().equals(123L) &&
                        apartmentDto.getYCoordinate().equals(321L) &&
                        apartmentDto.getApartmentNumber().equals(5) &&
                        apartmentDto.getStreet().equals("Kraszewskiego") &&
                        apartmentDto.getStreetNumber().equals(26) &&
                        apartmentDto.getUserId().equals(userId)));
    }

    @Test
    public void getUsersTest() {
        //Given
        Long userId = prepareAndSaveDataIntoDbAndReturnDataIds().get(0);
        //When
        List<UserDto> users = userDbFacade.getUsers();
        //Then
        assertEquals(1, users.size());
        assertEquals("Mateusz", users.get(0).getName());
        assertEquals("Jakuszko", users.get(0).getSurname());
        assertEquals("matanos", users.get(0).getUsername());
        assertEquals("mateusz.jakuszko@gmail.com", users.get(0).getEmail());
        assertEquals(userId, users.get(0).getId());
        assertTrue(passwordEncoder.matches("abc123", users.get(0).getPassword()));
        assertTrue(users.get(0).getReservations().stream()
                .allMatch(reservationDto -> reservationDto.getStartDate().equals(LocalDate.of(2020, 1, 12)) &&
                        reservationDto.getEndDate().equals(LocalDate.of(2020, 3, 14))));
        assertTrue(users.get(0).getApartments().stream()
                .allMatch(apartmentDto -> apartmentDto.getXCoordinate().equals(123L) &&
                        apartmentDto.getYCoordinate().equals(321L) &&
                        apartmentDto.getApartmentNumber().equals(5) &&
                        apartmentDto.getStreet().equals("Kraszewskiego") &&
                        apartmentDto.getStreetNumber().equals(26) &&
                        apartmentDto.getUserId().equals(userId)));
    }

    @Test
    public void createUserTest() {
        //Given
        User user = User.builder()
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .email("mateusz.jakuszko@gmail.com")
                .build();
        //When
        UserDto userDto = userMapper.mapToUserDto(user, new ArrayList<>(), new ArrayList<>());
        Long userId = userDbFacade.createUser(userDto).getId();
        UserDto createdUser = userDbFacade.getUser(userId);
        //Then
        assertEquals("Mateusz", createdUser.getName());
        assertEquals("Jakuszko", createdUser.getSurname());
        assertEquals("matanos", createdUser.getUsername());
        assertEquals("user", createdUser.getRole());
        assertEquals("mateusz.jakuszko@gmail.com", createdUser.getEmail());
        assertTrue(passwordEncoder.matches("abc123", createdUser.getPassword()));
    }

    @Test
    public void updateUserTest() {
        //Given
        List<Long> idsList = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = idsList.get(0);
        Long apartmentId = idsList.get(1);
        Long reservationId = idsList.get(2);
        Apartment apartment = apartmentDbService.getApartment(apartmentId).orElseThrow(NotFoundException::new);
        Reservation reservation = reservationDbService.gerReservation(reservationId).orElseThrow(NotFoundException::new);
        User user = userDbService.getUser(userId).orElseThrow(NotFoundException::new);
        ApartmentDto apartmentDto = apartmentMapper.mapToApartmentDto(apartment, Collections.singletonList(reservationId));
        ReservationDto reservationDto = reservationMapper.mapToReservationDto(reservation, apartmentDto);
        //When
        user.setName("Weronika");
        UserDto userDto = userMapper.mapToUserDto(user, Collections.singletonList(apartmentDto), Collections.singletonList(reservationDto));
        userDbFacade.updateUser(userDto);
        UserDto updatedUser = userDbFacade.getUser(userId);
        //Then
        assertEquals("Weronika", updatedUser.getName());
        assertEquals("Jakuszko", updatedUser.getSurname());
        assertEquals("matanos", updatedUser.getUsername());
        assertEquals("Admin", updatedUser.getRole());
        assertEquals("mateusz.jakuszko@gmail.com", updatedUser.getEmail());
        assertTrue(passwordEncoder.matches("abc123", updatedUser.getPassword()));
    }

    @Test(expected = NotFoundException.class)
    public void deleteUserTest() {
        //Given
        List<Long> idsList = prepareAndSaveDataIntoDbAndReturnDataIds();
        Long userId = idsList.get(0);
        Long apartmentId = idsList.get(1);
        Long reservationId = idsList.get(2);
        //When
        userDbFacade.deleteUser(userId);
        //Then
        userDbFacade.getUser(userId);
        assertFalse(userDbService.getUser(userId).isPresent());
        assertFalse(apartmentDbService.getApartment(apartmentId).isPresent());
        assertFalse(reservationDbService.gerReservation(reservationId).isPresent());
    }
}
