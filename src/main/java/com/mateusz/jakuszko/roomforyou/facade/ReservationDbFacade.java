package com.mateusz.jakuszko.roomforyou.facade;

import com.mateusz.jakuszko.roomforyou.domain.*;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import com.mateusz.jakuszko.roomforyou.mapper.ApartmentMapper;
import com.mateusz.jakuszko.roomforyou.mapper.ReservationMapper;
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService;
import com.mateusz.jakuszko.roomforyou.service.ReservationDbService;
import com.mateusz.jakuszko.roomforyou.service.UserDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservationDbFacade {

    private final UserDbService userDbService;
    private final ApartmentDbService apartmentDbService;
    private final ApartmentMapper apartmentMapper;
    private final ReservationDbService reservationDbService;
    private final ReservationMapper reservationMapper;


    public ReservationDto getReservation(Long reservationId) {
        Reservation reservation = reservationDbService.gerReservation(reservationId)
                .orElseThrow(NotFoundException::new);
        Apartment apartment = reservation.getApartment();
        ApartmentDto apartmentDto = apartmentMapper
                .mapToApartmentDto(apartment, createReservationsIds(apartment.getReservations()));
        return reservationMapper.mapToReservationDto(reservation, apartmentDto);
    }

    public List<ReservationDto> getReservations() {
        List<Reservation> reservations = reservationDbService.getReservations();
        List<Apartment> apartments = apartmentDbService.getApartments();
        List<ApartmentDto> apartmentDtos = apartments.stream()
                .map(apartment -> apartmentMapper.mapToApartmentDto(apartment, createReservationsIds(apartment.getReservations())))
                .collect(Collectors.toList());
        return reservationMapper.mapToReservationDtos(reservations, apartmentDtos);
    }

    @Transactional
    public ReservationDto createReservation(ReservationDto reservationDto) {
        User user = userDbService.getUser(reservationDto.getUserId()).orElseThrow(NotFoundException::new);
        Apartment apartment = apartmentDbService.getApartment(reservationDto.getApartmentDto().getId())
                .orElseThrow(NotFoundException::new);
        Reservation reservation = reservationMapper.mapToReservation(reservationDto, apartment, user);
        List<Reservation> reservationsInApartment = apartment.getReservations();
        reservationsInApartment.add(reservation);
        apartment.setReservations(reservationsInApartment);
        reservationDbService.save(reservation);
        apartmentDbService.update(apartment);
        userDbService.update(user);
        return reservationDto;
    }

    @Transactional
    public ReservationDto updateReservation(ReservationDto reservationDto) {
        User user = userDbService.getUser(reservationDto.getUserId()).orElseThrow(NotFoundException::new);
        Apartment apartment = apartmentDbService.getApartment(reservationDto.getApartmentDto().getId())
                .orElseThrow(NotFoundException::new);
        Reservation reservation = reservationMapper.mapToReservation(reservationDto, apartment, user);
        reservationDbService.update(reservation);
        return reservationDto;
    }

    public void deleteReservation(Long id) {
        Reservation reservation = reservationDbService.gerReservation(id).orElseThrow(NotFoundException::new);
        Long apartmentId = reservation.getApartment().getId();
        Long userId = reservation.getUser().getId();
        reservationDbService.delete(id);
        Apartment apartment = apartmentDbService.getApartment(apartmentId).orElseThrow(NotFoundException::new);
        List<Reservation> reservationsInApartment = apartment.getReservations();
        reservationsInApartment.remove(reservation);
        User user = userDbService.getUser(userId).orElseThrow(NotFoundException::new);
        List<Reservation> reservationsInUser = user.getReservations();
        reservationsInUser.remove(reservation);
        apartmentDbService.update(apartment);
        userDbService.update(user);
    }

    private List<Long> createReservationsIds(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservation -> reservation.getId())
                .collect(Collectors.toList());
    }
}
