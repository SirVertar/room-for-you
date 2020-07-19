package com.mateusz.jakuszko.roomforyou.mapper;

import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ReservationMapper {
    public ReservationDto mapToReservationDto(Reservation reservation) {
        log.info("Map Reservation to ReservationDto");
        return ReservationDto.builder()
                .id(reservation.getId())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .apartmentId(reservation.getApartment().getId())
                .customerId(reservation.getCustomer().getId())
                .build();
    }

    public Reservation mapToReservation(ReservationDto reservationDto, Apartment apartment, Customer customer) {
        log.info("Map ReservationDto to Reservation");
        return Reservation.builder()
                .id(reservationDto.getId())
                .startDate(reservationDto.getStartDate())
                .endDate(reservationDto.getEndDate())
                .apartment(apartment)
                .customer(customer)
                .build();
    }

    public List<ReservationDto> mapToReservationDtos(List<Reservation> reservations) {
        log.info("Map Reservations to ReservationDtos");
        return reservations.stream()
                .map(reservation -> ReservationDto.builder()
                        .id(reservation.getId())
                        .startDate(reservation.getStartDate())
                        .endDate(reservation.getEndDate())
                        .apartmentId(reservations.stream()
                                .map(e -> e.getApartment().getId())
                                .findAny().orElseThrow(NotFoundException::new))
                        .customerId(reservation.getCustomer().getId())
                        .build())
                .collect(Collectors.toList());
    }
}
