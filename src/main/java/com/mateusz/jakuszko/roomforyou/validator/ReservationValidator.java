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

    public boolean checkIsEndDateAfterStartDateAndStartDateAfterNow(ReservationDto reservationDto) {
        LocalDate startDate = reservationDto.getStartDate();
        LocalDate endDate = reservationDto.getEndDate();
        return startDate != null && endDate != null && startDate.isAfter(LocalDate.now().minusDays(1)) && startDate.isBefore(endDate.plusDays(1));
    }

    public boolean areReservationDatesGiven(ReservationDto reservationDto) {
        return reservationDto.getStartDate() != null && reservationDto.getEndDate() != null;
    }
}
