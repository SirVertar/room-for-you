package com.mateusz.jakuszko.roomforyou.mapper;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.dto.CustomerDto;
import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CustomerMapper {

    public CustomerDto mapToCustomerDto(Customer customer, List<ApartmentDto> apartmentDtos, List<ReservationDto> reservationDtos) {
        log.info("Map User to UserDto");
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .password(customer.getPassword())
                .username(customer.getUsername())
                .email(customer.getEmail())
                .role(customer.getRole())
                .apartmentDtos(apartmentDtos)
                .reservations(reservationDtos)
                .build();
    }

    public Customer mapToCustomer(CustomerDto customerDto, List<Apartment> apartments, List<Reservation> reservations) {
        log.info("Map UserDto to User");
        return Customer.builder()
                .id(customerDto.getId())
                .name(customerDto.getName())
                .surname(customerDto.getSurname())
                .password(customerDto.getPassword())
                .username(customerDto.getUsername())
                .email(customerDto.getEmail())
                .role(customerDto.getRole())
                .apartments(apartments)
                .reservations(reservations)
                .build();
    }

    public List<CustomerDto> mapToCustomerDtos(List<Customer> customers, List<ApartmentDto> apartmentDtos, List<ReservationDto> reservationDtos) {
        log.info("Map Users to UserDtos");
        return customers.stream()
                .map(user -> CustomerDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .surname(user.getSurname())
                        .password(user.getPassword())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .apartmentDtos(apartmentDtos.stream()
                                .filter(apartmentDto -> apartmentDto.getCustomerId().equals(user.getId()))
                                .collect(Collectors.toList()))
                        .reservations(reservationDtos.stream()
                                .filter(reservationDto -> reservationDto.getUserId().equals(user.getId()))
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
