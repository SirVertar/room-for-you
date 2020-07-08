package com.mateusz.jakuszko.roomforyou.facade;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.dto.CustomerDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import com.mateusz.jakuszko.roomforyou.mapper.ApartmentMapper;
import com.mateusz.jakuszko.roomforyou.mapper.ReservationMapper;
import com.mateusz.jakuszko.roomforyou.mapper.CustomerMapper;
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService;
import com.mateusz.jakuszko.roomforyou.service.ReservationDbService;
import com.mateusz.jakuszko.roomforyou.service.CustomerDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerDbFacade {

    private final CustomerDbService customerDbService;
    private final CustomerMapper customerMapper;
    private final ApartmentDbService apartmentDbService;
    private final ApartmentMapper apartmentMapper;
    private final ReservationDbService reservationDbService;
    private final ReservationMapper reservationMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CustomerDto getCustomer(Long id) {
        log.info("Get user by id - " + id);
        Customer customer = customerDbService.getUser(id).orElseThrow(NotFoundException::new);
        List<Apartment> apartments = customer.getApartments();
        List<Reservation> reservations = customer.getReservations();
        List<ApartmentDto> apartmentDtos = apartmentMapper.mapToApartmentDtos(apartments);
        List<ReservationDto> reservationDtos = reservationMapper
                .mapToReservationDtos(reservations, apartmentDtos);
        return customerMapper.mapToCustomerDto(customer, apartmentDtos, reservationDtos);

    }

    // TODO------------!!!!
    @Transactional
    public List<CustomerDto> getCustomers() {
        log.info("Get users");
        List<Customer> customers = customerDbService.getUsers();
        List<Reservation> reservations = reservationDbService.getReservations();
        List<Apartment> apartments = apartmentDbService.getApartments();
        List<ApartmentDto> apartmentDtos = apartmentMapper.mapToApartmentDtos(apartments);
        List<ReservationDto> reservationDtos = reservationMapper.mapToReservationDtos(reservations, apartmentDtos);
        return customerMapper.mapToCustomerDtos(customers, apartmentDtos, reservationDtos);
    }

    @Transactional
    public CustomerDto createCustomer(CustomerDto customerDto) {
        log.info("Create User_" + "username: " + customerDto.getUsername());
        customerDto.setRole("user");
        return customerMapper.mapToCustomerDto
                (customerDbService.save(customerMapper.mapToCustomer(customerDto, new ArrayList<>(), new ArrayList<>()), passwordEncoder),
                        new ArrayList<>(), new ArrayList<>());
    }

    @Transactional
    public CustomerDto updateCustomer(CustomerDto customerDto) {
        log.info("Update user_" + "username: " + customerDto.getUsername());
        List<Apartment> apartments = apartmentDbService.findByUserId(customerDto.getId());
        List<Reservation> reservations = reservationDbService.getReservationsByUserId(customerDto.getId());
        List<ApartmentDto> apartmentDtos = apartmentMapper.mapToApartmentDtos(apartments);
        List<ReservationDto> reservationDtos = reservationMapper.mapToReservationDtos(reservations,
                apartmentDtos);
        Customer customer = customerMapper.mapToCustomer(customerDto, apartments, reservations);
        Customer updatedCustomer = customerDbService.update(customer);
        return customerMapper.mapToCustomerDto(updatedCustomer, apartmentDtos, reservationDtos);
    }

    @Transactional
    public void deleteCustomer(Long userId) {
        log.info("Delete Customer - " + userId);
        reservationDbService.deleteReservationsByUserId(userId);
        apartmentDbService.deleteApartmentsByUserId(userId);
        customerDbService.delete(userId);
    }
}