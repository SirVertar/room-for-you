package com.mateusz.jakuszko.roomforyou.mapper;

import com.mateusz.jakuszko.roomforyou.domain.*;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationMapper {
    public ReservationDto mapToReservationDto(Reservation reservation, ApartmentDto apartmentDto) {
        return ReservationDto.builder()
                .id(reservation.getId())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .apartmentDto(apartmentDto)
                .userId(reservation.getUser().getId())
                .build();
    }

    public Reservation mapToReservation(ReservationDto reservationDto, Apartment apartment, User user) {
        return Reservation.builder()
                .id(reservationDto.getId())
                .startDate(reservationDto.getStartDate())
                .endDate(reservationDto.getEndDate())
                .apartment(apartment)
                .user(user)
                .build();
    }

    public List<ReservationDto> mapToReservationDtos(List<Reservation> reservations, List<ApartmentDto> apartmentDtos) {
        return reservations.stream()
                .map(reservation -> ReservationDto.builder()
                        .id(reservation.getId())
                        .startDate(reservation.getStartDate())
                        .endDate(reservation.getEndDate())
                        .apartmentDto(apartmentDtos.stream()
                                .filter(apartmentDto -> apartmentDto.getId().equals(reservation.getApartment().getId()))
                                .findAny().orElseThrow(NotFoundException::new))
                        .userId(reservation.getUser().getId())
                        .build())
                .collect(Collectors.toList());
    }
}
