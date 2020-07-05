package com.mateusz.jakuszko.roomforyou.mapper;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
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
                .userId(reservation.getCustomer().getId())
                .build();
    }

    public Reservation mapToReservation(ReservationDto reservationDto, Apartment apartment, Customer customer) {
        return Reservation.builder()
                .id(reservationDto.getId())
                .startDate(reservationDto.getStartDate())
                .endDate(reservationDto.getEndDate())
                .apartment(apartment)
                .customer(customer)
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
                        .userId(reservation.getCustomer().getId())
                        .build())
                .collect(Collectors.toList());
    }
}
