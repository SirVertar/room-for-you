package com.mateusz.jakuszko.roomforyou.mapper.deleted;

import com.mateusz.jakuszko.roomforyou.dto.deleted.DeletedReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedReservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DeletedReservationMapper {

    public DeletedReservation mapToDeletedReservation(Reservation reservation) {
        return DeletedReservation.builder()
                .previousReservationId(reservation.getId())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .previousCustomerId(reservation.getCustomer().getId())
                .previousCustomerId(reservation.getApartment().getId()).build();
    }

    public List<DeletedReservation> mapToDeletedReservations(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservation -> DeletedReservation.builder()
                        .previousReservationId(reservation.getId())
                        .startDate(reservation.getStartDate())
                        .endDate(reservation.getEndDate())
                        .previousCustomerId(reservation.getCustomer().getId())
                        .previousApartmentId(reservation.getApartment().getId()).build())
                .collect(Collectors.toList());
    }

    public List<DeletedReservationDto> mapToDeletedReservationDtos(List<DeletedReservation> reservations) {
        return reservations.stream()
                .map(reservation -> DeletedReservationDto.builder()
                        .id(reservation.getId())
                        .previousReservationId(reservation.getPreviousReservationId())
                        .previousApartmentId(reservation.getPreviousApartmentId())
                        .previousCustomerId(reservation.getPreviousCustomerId())
                        .startDate(reservation.getStartDate())
                        .endDate(reservation.getEndDate()).build())
                .collect(Collectors.toList());
    }
}
