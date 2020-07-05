package com.mateusz.jakuszko.roomforyou.mapper;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.dto.UserDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto mapToUserDto(Customer customer, List<ApartmentDto> apartmentDtos, List<ReservationDto> reservationDtos) {
        return UserDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .password(customer.getPassword())
                .username(customer.getUsername())
                .email(customer.getEmail())
                .role(customer.getRole())
                .apartments(apartmentDtos)
                .reservations(reservationDtos)
                .build();
    }

    public Customer mapToUser(UserDto userDto, List<Apartment> apartments, List<Reservation> reservations) {
        return Customer.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .surname(userDto.getSurname())
                .password(userDto.getPassword())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .role(userDto.getRole())
                .apartments(apartments)
                .reservations(reservations)
                .build();
    }

    public List<UserDto> mapToUserDtos(List<Customer> customers, List<ApartmentDto> apartmentDtos, List<ReservationDto> reservationDtos) {
        return customers.stream()
                .map(user -> UserDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .surname(user.getSurname())
                        .password(user.getPassword())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .apartments(apartmentDtos.stream()
                                .filter(apartmentDto -> apartmentDto.getUserId().equals(user.getId()))
                                .collect(Collectors.toList()))
                        .reservations(reservationDtos.stream()
                                .filter(reservationDto -> reservationDto.getUserId().equals(user.getId()))
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
