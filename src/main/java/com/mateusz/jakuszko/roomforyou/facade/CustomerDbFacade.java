package com.mateusz.jakuszko.roomforyou.facade;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.dto.CustomerDto;
import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedApartment;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedCustomer;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedReservation;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import com.mateusz.jakuszko.roomforyou.mapper.ApartmentMapper;
import com.mateusz.jakuszko.roomforyou.mapper.CustomerMapper;
import com.mateusz.jakuszko.roomforyou.mapper.ReservationMapper;
import com.mateusz.jakuszko.roomforyou.mapper.deleted.DeletedApartmentMapper;
import com.mateusz.jakuszko.roomforyou.mapper.deleted.DeletedCustomerMapper;
import com.mateusz.jakuszko.roomforyou.mapper.deleted.DeletedReservationMapper;
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService;
import com.mateusz.jakuszko.roomforyou.service.CustomerDbService;
import com.mateusz.jakuszko.roomforyou.service.ReservationDbService;
import com.mateusz.jakuszko.roomforyou.service.deleted.DeletedApartmentDbService;
import com.mateusz.jakuszko.roomforyou.service.deleted.DeletedCustomerDbService;
import com.mateusz.jakuszko.roomforyou.service.deleted.DeletedReservationDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

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
    private final DeletedApartmentMapper deletedApartmentMapper;
    private final DeletedCustomerMapper deletedCustomerMapper;
    private final DeletedReservationMapper deletedReservationMapper;
    private final DeletedApartmentDbService deletedApartmentDbService;
    private final DeletedCustomerDbService deletedCustomerDbService;
    private final DeletedReservationDbService deletedReservationDbService;

    @Transactional
    public CustomerDto getCustomer(Long id) {
        log.info("Get Customer by id - " + id);
        Customer customer = customerDbService.getUser(id).orElseThrow(NotFoundException::new);
        List<Apartment> apartments = customer.getApartments();
        List<Reservation> reservations = customer.getReservations();
        List<ApartmentDto> apartmentDtos = apartmentMapper.mapToApartmentDtos(apartments);
        List<ReservationDto> reservationDtos = reservationMapper
                .mapToReservationDtos(reservations);
        return customerMapper.mapToCustomerDto(customer, apartmentDtos, reservationDtos);
    }

    @Transactional
    public List<CustomerDto> getCustomers() {
        log.info("Get Customers");
        List<Customer> customers = customerDbService.getUsers();
        List<Reservation> reservations = reservationDbService.getReservations();
        List<Apartment> apartments = apartmentDbService.getApartments();
        List<ApartmentDto> apartmentDtos = apartmentMapper.mapToApartmentDtos(apartments);
        List<ReservationDto> reservationDtos = reservationMapper.mapToReservationDtos(reservations);
        return customerMapper.mapToCustomerDtos(customers, apartmentDtos, reservationDtos);
    }

    @Transactional
    public CustomerDto createCustomer(CustomerDto customerDto) {
        log.info("Create Customer" + "username: " + customerDto.getUsername());
        customerDto.setRole("user");
        return customerMapper.mapToCustomerDto
                (customerDbService.save(customerMapper.mapToCustomer(customerDto, new ArrayList<>(), new ArrayList<>()), passwordEncoder),
                        new ArrayList<>(), new ArrayList<>());
    }

    @Transactional
    public CustomerDto updateCustomer(CustomerDto customerDto) {
        log.info("Update Customer" + "username: " + customerDto.getUsername());
        List<Apartment> apartments = apartmentDbService.findByUserId(customerDto.getId());
        List<Reservation> reservations = reservationDbService.getReservationsByUserId(customerDto.getId());
        List<ApartmentDto> apartmentDtos = apartmentMapper.mapToApartmentDtos(apartments);
        List<ReservationDto> reservationDtos = reservationMapper.mapToReservationDtos(reservations);
        Customer customer = customerMapper.mapToCustomer(customerDto, apartments, reservations);
        Customer updatedCustomer = customerDbService.update(customer);
        return customerMapper.mapToCustomerDto(updatedCustomer, apartmentDtos, reservationDtos);
    }

    @Transactional
    public void deleteCustomer(Long userId) {
        log.info("Delete Customer - " + userId);
        Optional<Customer> customer = customerDbService.getUser(userId);
        if (!customer.isPresent()) {
            log.error("Customer doesn't exist");
            throw new NotFoundException();
        }
        saveDeletedInformationAboutCustomer(customer.get());
        Set<Long> deletedReservation = new HashSet<>();
        for (Reservation reservation : customer.get().getReservations()) {
            deletedReservation.add(reservation.getId());
            reservationDbService.delete(reservation.getId());
        }
        for (Apartment apartment : customer.get().getApartments()) {
            for (Reservation reservation : apartment.getReservations()) {
                if (!deletedReservation.contains(reservation.getId())) {
                    reservationDbService.delete(reservation.getId());
                    deletedReservation.add(reservation.getId());
                }
            }
        }
        apartmentDbService.deleteApartmentsByUserId(userId);
        customerDbService.delete(userId);
    }

    public void saveDeletedInformationAboutCustomer(Customer customer) {
        List<DeletedReservation> deletedReservations = new ArrayList<>();
        if (customer.getReservations().size() != 0) {
            deletedReservations = deletedReservationMapper
                    .mapToDeletedReservations(customer.getReservations());
        }
        List<DeletedApartment> deletedApartments;
        if (customer.getApartments().size() != 0) {
            deletedApartments = deletedApartmentMapper
                    .mapToDeleteApartments(customer.getApartments(), deletedReservations);
        } else {
            deletedApartments = deletedApartmentMapper
                    .mapToDeleteApartments(customer.getApartments());
        }
        DeletedCustomer deletedCustomer = deletedCustomerMapper
                .mapToDeletedCustomer(customer, deletedReservations, deletedApartments);
        for (DeletedReservation reservation : deletedReservations) {
            deletedReservationDbService.save(reservation);
        }
        for (DeletedApartment apartment : deletedApartments) {
            deletedApartmentDbService.save(apartment);
        }
        deletedCustomerDbService.save(deletedCustomer, passwordEncoder);
    }
}
