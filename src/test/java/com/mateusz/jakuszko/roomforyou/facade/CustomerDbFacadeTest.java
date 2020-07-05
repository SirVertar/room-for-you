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
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerDbFacadeTest {

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
                .allMatch(apartmentDto -> apartmentDto.getLatitude().equals(123L) &&
                        apartmentDto.getLongitude().equals(321L) &&
                        apartmentDto.getApartmentNumber().equals(5) &&
                        apartmentDto.getCity().equals("Terespol") &&
                        apartmentDto.getStreet().equals("Kraszewskiego") &&
                        apartmentDto.getStreetNumber().equals("26") &&
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
                .allMatch(apartmentDto -> apartmentDto.getLatitude().equals(123L) &&
                        apartmentDto.getLongitude().equals(321L) &&
                        apartmentDto.getApartmentNumber().equals(5) &&
                        apartmentDto.getCity().equals("Terespol") &&
                        apartmentDto.getStreet().equals("Kraszewskiego") &&
                        apartmentDto.getStreetNumber().equals("26") &&
                        apartmentDto.getUserId().equals(userId)));
    }

    @Test
    public void createUserTest() {
        //Given
        Customer customer = Customer.builder()
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .email("mateusz.jakuszko@gmail.com")
                .build();
        //When
        UserDto userDto = userMapper.mapToUserDto(customer, new ArrayList<>(), new ArrayList<>());
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
        Customer customer = userDbService.getUser(userId).orElseThrow(NotFoundException::new);
        ApartmentDto apartmentDto = apartmentMapper.mapToApartmentDto(apartment, Collections.singletonList(reservationId));
        ReservationDto reservationDto = reservationMapper.mapToReservationDto(reservation, apartmentDto);
        //When
        customer.setName("Weronika");
        UserDto userDto = userMapper.mapToUserDto(customer, Collections.singletonList(apartmentDto), Collections.singletonList(reservationDto));
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
