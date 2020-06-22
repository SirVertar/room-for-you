package com.mateusz.jakuszko.roomforyou.mapper;

import com.mateusz.jakuszko.roomforyou.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto mapToUserDto(User user, List<ApartmentDto> apartmentDtos, List<ReservationDto> reservationDtos) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .password(user.getPassword())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .apartments(apartmentDtos)
                .reservations(reservationDtos)
                .build();
    }

    public User mapToUser(UserDto userDto, List<Apartment> apartments, List<Reservation> reservations) {
        return User.builder()
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

    public List<UserDto> mapToUserDtos(List<User> users, List<ApartmentDto> apartmentDtos, List<ReservationDto> reservationDtos) {
        return users.stream()
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
