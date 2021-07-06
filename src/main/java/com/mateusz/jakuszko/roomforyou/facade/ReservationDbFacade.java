package com.mateusz.jakuszko.roomforyou.facade;

import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedReservation;
import com.mateusz.jakuszko.roomforyou.exceptions.InvalidReservationDateException;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import com.mateusz.jakuszko.roomforyou.mapper.ReservationMapper;
import com.mateusz.jakuszko.roomforyou.mapper.deleted.DeletedReservationMapper;
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService;
import com.mateusz.jakuszko.roomforyou.service.CustomerDbService;
import com.mateusz.jakuszko.roomforyou.service.ReservationDbService;
import com.mateusz.jakuszko.roomforyou.service.deleted.DeletedReservationDbService;
import com.mateusz.jakuszko.roomforyou.validator.ReservationValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationDbFacade {
    private final CustomerDbService customerDbService;
    private final ApartmentDbService apartmentDbService;
    private final ReservationDbService reservationDbService;
    private final ReservationMapper reservationMapper;
    private final ReservationValidator reservationValidator;
    private final DeletedReservationMapper deletedReservationMapper;
    private final DeletedReservationDbService deletedReservationDbService;

    public ReservationDto getReservation(Long reservationId) {
        log.info("Get Reservation by id - " + reservationId);
        Reservation reservation = reservationDbService.getReservation(reservationId)
                .orElseThrow(NotFoundException::new);
        return reservationMapper.mapToReservationDto(reservation);
    }

    public List<ReservationDto> getReservations() {
        log.info("Get All reservations");
        List<Reservation> reservations = reservationDbService.getReservations();
        return reservationMapper.mapToReservationDtos(reservations);
    }

    @Transactional
    public ReservationDto createReservation(ReservationDto reservationDto) {
        log.info("Create Reservation_" +
                "Apartment_Id - " + reservationDto.getApartmentId() +
                ", Start_Date - " + reservationDto.getStartDate() +
                ", End_date - " + reservationDto.getEndDate());
        if (!reservationValidator.checkIsTherePossibilityToMakeReservation(reservationDto) ||
                !reservationValidator.checkIsEndDateAfterStartDateAndStartDateAfterNow(reservationDto)) {
            throw new InvalidReservationDateException();
        }
        Customer customer = customerDbService.getUser(reservationDto.getCustomerId()).orElseThrow(NotFoundException::new);
        Apartment apartment = apartmentDbService.getApartment(reservationDto.getApartmentId())
                .orElseThrow(NotFoundException::new);
        Reservation reservation = reservationMapper.mapToReservation(reservationDto, apartment, customer);
        List<Reservation> reservationsInApartment = apartment.getReservations();
        reservationsInApartment.add(reservation);
        apartment.setReservations(reservationsInApartment);
        reservationDbService.save(reservation);
        apartmentDbService.update(apartment);
        customerDbService.update(customer);
        return reservationMapper.mapToReservationDto(reservation);
    }

    @Transactional
    public ReservationDto updateReservation(ReservationDto reservationDto) {
        log.info("Update Reservation_" +
                "Apartment_Id - " + reservationDto.getApartmentId() +
                ", Start_Date - " + reservationDto.getStartDate() +
                ", End_date - " + reservationDto.getEndDate());
        Customer customer = customerDbService.getUser(reservationDto.getCustomerId()).orElseThrow(NotFoundException::new);
        Apartment apartment = apartmentDbService.getApartment(reservationDto.getApartmentId())
                .orElseThrow(NotFoundException::new);
        Reservation reservation = reservationMapper.mapToReservation(reservationDto, apartment, customer);
        reservationDbService.update(reservation);
        return reservationDto;
    }

    public void deleteReservation(Long id) {
        log.info("Delete Reservation by id - " + id);
        Reservation reservation = reservationDbService.getReservation(id).orElseThrow(NotFoundException::new);
        saveDeletedInformationAboutReservation(reservation);
        Long apartmentId = reservation.getApartment().getId();
        Long userId = reservation.getCustomer().getId();
        reservationDbService.delete(id);
        Apartment apartment = apartmentDbService.getApartment(apartmentId).orElseThrow(NotFoundException::new);
        List<Reservation> reservationsInApartment = apartment.getReservations();
        reservationsInApartment.remove(reservation);
        Customer customer = customerDbService.getUser(userId).orElseThrow(NotFoundException::new);
        List<Reservation> reservationsInUser = customer.getReservations();
        reservationsInUser.remove(reservation);
        apartmentDbService.update(apartment);
        customerDbService.update(customer);
    }

    public void saveDeletedInformationAboutReservation(Reservation reservation) {
        DeletedReservation deletedReservation = deletedReservationMapper.mapToDeletedReservation(reservation);
        deletedReservationDbService.save(deletedReservation);
    }
}
