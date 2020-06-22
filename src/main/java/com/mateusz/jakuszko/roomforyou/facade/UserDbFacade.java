package com.mateusz.jakuszko.roomforyou.facade;

import com.mateusz.jakuszko.roomforyou.domain.*;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import com.mateusz.jakuszko.roomforyou.mapper.ApartmentMapper;
import com.mateusz.jakuszko.roomforyou.mapper.ReservationMapper;
import com.mateusz.jakuszko.roomforyou.mapper.UserMapper;
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService;
import com.mateusz.jakuszko.roomforyou.service.ReservationDbService;
import com.mateusz.jakuszko.roomforyou.service.UserDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDbFacade {

    private final UserDbService userDbService;
    private final UserMapper userMapper;
    private final ApartmentDbService apartmentDbService;
    private final ApartmentMapper apartmentMapper;
    private final ReservationDbService reservationDbService;
    private final ReservationMapper reservationMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto getUser(Long id) {
        User user = userDbService.getUser(id).orElseThrow(NotFoundException::new);
        List<Apartment> apartments = user.getApartments();
        List<Reservation> reservations = user.getReservations();
        List<ApartmentDto> apartmentDtos = apartmentMapper.mapToApartmentDtos(apartments);
        List<ReservationDto> reservationDtos = reservationMapper
                .mapToReservationDtos(reservations, apartmentDtos);
        return userMapper.mapToUserDto(user, apartmentDtos, reservationDtos);

    }

    // TODO------------!!!!
    @Transactional
    public List<UserDto> getUsers() {
        List<User> users = userDbService.getUsers();
        List<Reservation> reservations = reservationDbService.getReservations();
        List<Apartment> apartments = apartmentDbService.getApartments();
        List<ApartmentDto> apartmentDtos = apartmentMapper.mapToApartmentDtos(apartments);
        List<ReservationDto> reservationDtos = reservationMapper.mapToReservationDtos(reservations, apartmentDtos);
        return userMapper.mapToUserDtos(users, apartmentDtos, reservationDtos);
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        userDto.setRole("user");
        return userMapper.mapToUserDto
                (userDbService.save(userMapper.mapToUser(userDto, new ArrayList<>(), new ArrayList<>()), passwordEncoder),
                        new ArrayList<>(), new ArrayList<>());
    }

    @Transactional
    public UserDto updateUser(UserDto userDto) {
        List<Apartment> apartments = apartmentDbService.findByUserId(userDto.getId());
        List<Reservation> reservations = reservationDbService.getReservationsByUserId(userDto.getId());
        List<ApartmentDto> apartmentDtos = apartmentMapper.mapToApartmentDtos(apartments);
        List<ReservationDto> reservationDtos = reservationMapper.mapToReservationDtos(reservations,
                apartmentDtos);
        User user = userMapper.mapToUser(userDto, apartments, reservations);
        User updatedUser = userDbService.update(user);
        return userMapper.mapToUserDto(updatedUser, apartmentDtos, reservationDtos);
    }

    @Transactional
    public void deleteUser(Long userId) {
        apartmentDbService.deleteApartmentsByUserId(userId);
        reservationDbService.deleteReservationsByUserId(userId);
        userDbService.delete(userId);
    }
}
