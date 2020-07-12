package com.mateusz.jakuszko.roomforyou.validator;

import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.service.ReservationDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationValidator {
    private final ReservationDbService reservationDbService;

    public boolean checkIsTherePossibilityToMakeReservation(ReservationDto reservationDto) {
        LocalDate startDate = reservationDto.getStartDate();
        LocalDate endDate = reservationDto.getEndDate();
        List<Reservation> reservations = reservationDbService
                .getReservationsByApartmentId(reservationDto.getApartmentId());
        return reservations.stream().noneMatch(reservation -> reservation.getStartDate().isBefore(endDate) &&
                reservation.getEndDate().isAfter(startDate));
    }
}
