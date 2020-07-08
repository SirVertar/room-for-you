package com.mateusz.jakuszko.roomforyou.facade;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import com.mateusz.jakuszko.roomforyou.mapper.ApartmentMapper;
import com.mateusz.jakuszko.roomforyou.mapper.ReservationMapper;
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService;
import com.mateusz.jakuszko.roomforyou.service.ReservationDbService;
import com.mateusz.jakuszko.roomforyou.service.CustomerDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservationDbFacade {

    private final CustomerDbService customerDbService;
    private final ApartmentDbService apartmentDbService;
    private final ApartmentMapper apartmentMapper;
    private final ReservationDbService reservationDbService;
    private final ReservationMapper reservationMapper;


    public ReservationDto getReservation(Long reservationId) {
        log.info("Get Reservation by id - " + reservationId);
        Reservation reservation = reservationDbService.gerReservation(reservationId)
                .orElseThrow(NotFoundException::new);
        Apartment apartment = reservation.getApartment();
        ApartmentDto apartmentDto = apartmentMapper
                .mapToApartmentDto(apartment);
        return reservationMapper.mapToReservationDto(reservation, apartmentDto);
    }

    public List<ReservationDto> getReservations() {
        log.info("Get All reservations");
        List<Reservation> reservations = reservationDbService.getReservations();
        List<Apartment> apartments = apartmentDbService.getApartments();
        List<ApartmentDto> apartmentDtos = apartments.stream()
                .map(apartmentMapper::mapToApartmentDto)
                .collect(Collectors.toList());
        return reservationMapper.mapToReservationDtos(reservations, apartmentDtos);
    }

    @Transactional
    public ReservationDto createReservation(ReservationDto reservationDto) {
        log.info("Create Reservation_" +
                "Apartment_Id - " + reservationDto.getApartmentDto().getId() +
                ", Start_Date - " + reservationDto.getStartDate() +
                ", End_date - " + reservationDto.getEndDate());
        Customer customer = customerDbService.getUser(reservationDto.getUserId()).orElseThrow(NotFoundException::new);
        Apartment apartment = apartmentDbService.getApartment(reservationDto.getApartmentDto().getId())
                .orElseThrow(NotFoundException::new);
        Reservation reservation = reservationMapper.mapToReservation(reservationDto, apartment, customer);
        List<Reservation> reservationsInApartment = apartment.getReservations();
        reservationsInApartment.add(reservation);
        apartment.setReservations(reservationsInApartment);
        reservationDbService.save(reservation);
        apartmentDbService.update(apartment);
        customerDbService.update(customer);
        return reservationDto;
    }

    @Transactional
    public ReservationDto updateReservation(ReservationDto reservationDto) {
        log.info("Update Reservation_" +
                "Apartment_Id - " + reservationDto.getApartmentDto().getId() +
                ", Start_Date - " + reservationDto.getStartDate() +
                ", End_date - " + reservationDto.getEndDate());
        Customer customer = customerDbService.getUser(reservationDto.getUserId()).orElseThrow(NotFoundException::new);
        Apartment apartment = apartmentDbService.getApartment(reservationDto.getApartmentDto().getId())
                .orElseThrow(NotFoundException::new);
        Reservation reservation = reservationMapper.mapToReservation(reservationDto, apartment, customer);
        reservationDbService.update(reservation);
        return reservationDto;
    }

    public void deleteReservation(Long id) {
        log.info("Delete Reservation by id - " + id);
        Reservation reservation = reservationDbService.gerReservation(id).orElseThrow(NotFoundException::new);
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

    private List<Long> createReservationsIds(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservation -> reservation.getId())
                .collect(Collectors.toList());
    }
}