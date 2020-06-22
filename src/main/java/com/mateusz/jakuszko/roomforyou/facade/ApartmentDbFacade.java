package com.mateusz.jakuszko.roomforyou.facade;

import com.mateusz.jakuszko.roomforyou.domain.Apartment;
import com.mateusz.jakuszko.roomforyou.domain.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.domain.Reservation;
import com.mateusz.jakuszko.roomforyou.domain.User;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import com.mateusz.jakuszko.roomforyou.mapper.ApartmentMapper;
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
public class ApartmentDbFacade {

    private final UserDbService userDbService;
    private final ApartmentDbService apartmentDbService;
    private final ApartmentMapper apartmentMapper;
    private final ReservationDbService reservationDbService;

    @Transactional
    public ApartmentDto getApartment(Long apartmentId) {
        Apartment apartment = apartmentDbService.getApartment(apartmentId).orElseThrow(NotFoundException::new);
        List<Long> reservationsIds = apartment.getReservations().stream()
                .map(Reservation::getId)
                .collect(Collectors.toList());
        return apartmentMapper.mapToApartmentDto(apartment, reservationsIds);
    }

    @Transactional
    public List<ApartmentDto> getApartments() {
        List<Apartment> apartments = apartmentDbService.getApartments();
        List<Reservation> reservations = reservationDbService.getReservations();
        return apartmentMapper.mapToApartmentDtos(apartments);
    }

    @Transactional
    public ApartmentDto createApartment(ApartmentDto apartmentDto) {
        List<Reservation> reservations = reservationDbService.getReservationsByApartmentId(apartmentDto.getId());
        User user = userDbService.getUser(apartmentDto.getUserId()).orElseThrow(NotFoundException::new);
        Apartment apartment = apartmentMapper.mapToApartment(apartmentDto, reservations, user);
        List<Apartment> newUserApartmentList = userDbService.getUser(user.getId()).orElseThrow(NotFoundException::new).getApartments();
        newUserApartmentList.add(apartment);
        apartmentDbService.save(apartment);
        userDbService.update(user);
        return apartmentMapper.mapToApartmentDto(apartment, apartmentDto.getReservationsIds());
    }

    public ApartmentDto updateApartment(ApartmentDto apartmentDto) {
        return createApartment(apartmentDto);
    }

    // TODO Add functionality - After delete apartment and reservations from user -> save in other entity
    @Transactional
    public void deleteApartment(Long apartmentId) {
        Apartment apartment = apartmentDbService.getApartment(apartmentId).orElseThrow(NotFoundException::new);
        User user = apartment.getUser();
        Long reservationId = apartment.getReservations().stream()
                .map(Reservation::getId)
                .findAny().orElseThrow(NotFoundException::new);
        Reservation reservation = reservationDbService.gerReservation(reservationId).orElseThrow(NotFoundException::new);
        apartmentDbService.delete(apartmentId);
        user.getReservations().remove(reservation);
        user.getApartments().remove(apartment);
        userDbService.update(user);
        reservationDbService.delete(reservationId);
    }
}
