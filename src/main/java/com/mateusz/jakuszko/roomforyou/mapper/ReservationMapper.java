package com.mateusz.jakuszko.roomforyou.mapper;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
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
    public ReservationDto mapToReservationDto(Reservation reservation, ApartmentDto apartmentDto) {
        log.info("Map Reservation to ReservationDto");
        return ReservationDto.builder()
                .id(reservation.getId())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .apartmentId(apartmentDto.getId())
                .userId(reservation.getCustomer().getId())
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

    public List<ReservationDto> mapToReservationDtos(List<Reservation> reservations, List<ApartmentDto> apartmentDtos) {
        log.info("Map Reservations to ReservationDtos");
        return reservations.stream()
                .map(reservation -> ReservationDto.builder()
                        .id(reservation.getId())
                        .startDate(reservation.getStartDate())
                        .endDate(reservation.getEndDate())
                        .apartmentId(apartmentDtos.stream()
                                .filter(apartment -> apartment.getId().equals(reservation.getApartment().getId()))
                                .map(ApartmentDto::getId)
                                .findAny().orElseThrow(NotFoundException::new))
                        .userId(reservation.getCustomer().getId())
                        .build())
                .collect(Collectors.toList());
    }
}
