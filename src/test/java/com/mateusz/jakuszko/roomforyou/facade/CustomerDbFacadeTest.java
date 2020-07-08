package com.mateusz.jakuszko.roomforyou.facade;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.dto.CustomerDto;
import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import com.mateusz.jakuszko.roomforyou.mapper.ApartmentMapper;
import com.mateusz.jakuszko.roomforyou.mapper.CustomerMapper;
import com.mateusz.jakuszko.roomforyou.mapper.ReservationMapper;
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
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerDbFacadeTest {

    @Autowired
    private CustomerDbFacade customerDbFacade;
    @Autowired
    private CustomerDbService customerDbService;
    @Autowired
    private ApartmentDbService apartmentDbService;
    @Autowired
    private ReservationDbService reservationDbService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerMapper customerMapper;
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
                .latitude(123.0)
                .longitude(321.0)
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

        customerDbService.save(customer, passwordEncoder);
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
        CustomerDto customerDto = customerDbFacade.getCustomer(userId);
        //Then
        assertEquals("Mateusz", customerDto.getName());
        assertEquals("Jakuszko", customerDto.getSurname());
        assertEquals("matanos", customerDto.getUsername());
        assertEquals("mateusz.jakuszko@gmail.com", customerDto.getEmail());
        assertEquals(userId, customerDto.getId());
        assertTrue(passwordEncoder.matches("abc123", customerDto.getPassword()));
        assertTrue(customerDto.getReservations().stream()
                .allMatch(reservationDto -> reservationDto.getStartDate().equals(LocalDate.of(2020, 1, 12)) &&
                        reservationDto.getEndDate().equals(LocalDate.of(2020, 3, 14))));
        assertTrue(customerDto.getApartments().stream()
                .allMatch(apartmentDto -> apartmentDto.getLatitude().equals(123.0) &&
                        apartmentDto.getLongitude().equals(321.0) &&
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
        List<CustomerDto> users = customerDbFacade.getCustomers();
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
                .allMatch(apartmentDto -> apartmentDto.getLatitude().equals(123.0) &&
                        apartmentDto.getLongitude().equals(321.0) &&
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
        CustomerDto customerDto = customerMapper.mapToCustomerDto(customer, new ArrayList<>(), new ArrayList<>());
        Long userId = customerDbFacade.createCustomer(customerDto).getId();
        CustomerDto createdUser = customerDbFacade.getCustomer(userId);
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
        Customer customer = customerDbService.getUser(userId).orElseThrow(NotFoundException::new);
        ApartmentDto apartmentDto = apartmentMapper.mapToApartmentDto(apartment);
        ReservationDto reservationDto = reservationMapper.mapToReservationDto(reservation, apartmentDto);
        //When
        customer.setName("Weronika");
        CustomerDto customerDto = customerMapper.mapToCustomerDto(customer, Collections.singletonList(apartmentDto), Collections.singletonList(reservationDto));
        customerDbFacade.updateCustomer(customerDto);
        CustomerDto updatedUser = customerDbFacade.getCustomer(userId);
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
        customerDbFacade.deleteCustomer(userId);
        //Then
        customerDbFacade.getCustomer(userId);
        assertFalse(customerDbService.getUser(userId).isPresent());
        assertFalse(apartmentDbService.getApartment(apartmentId).isPresent());
        assertFalse(reservationDbService.gerReservation(reservationId).isPresent());
    }
}
