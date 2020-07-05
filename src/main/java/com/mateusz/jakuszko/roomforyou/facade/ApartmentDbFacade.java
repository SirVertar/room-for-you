package com.mateusz.jakuszko.roomforyou.facade;

import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import com.mateusz.jakuszko.roomforyou.mapper.ApartmentMapper;
import com.mateusz.jakuszko.roomforyou.opencagegeocoder.client.OpenCageGeocoderClient;
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
    private final OpenCageGeocoderClient openCageGeocoderClient;

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
        Customer customer = userDbService.getUser(apartmentDto.getUserId()).orElseThrow(NotFoundException::new);
        //Map<String, String> map =  openCageGeocoderClient.getUrlWithApartmentDetails(apartmentDto);
        Apartment apartment = apartmentMapper.mapToApartment(apartmentDto, reservations, customer);
        List<Apartment> newUserApartmentList = userDbService.getUser(customer.getId()).orElseThrow(NotFoundException::new).getApartments();
        newUserApartmentList.add(apartment);
        apartmentDbService.save(apartment);
        userDbService.update(customer);
        return apartmentMapper.mapToApartmentDto(apartment, apartmentDto.getReservationsIds());
    }

    public ApartmentDto updateApartment(ApartmentDto apartmentDto) {
        return createApartment(apartmentDto);
    }

    // TODO Add functionality - After delete apartment and reservations from user -> save in other entity
    @Transactional
    public void deleteApartment(Long apartmentId) {
        Apartment apartment = apartmentDbService.getApartment(apartmentId).orElseThrow(NotFoundException::new);
        Customer customer = apartment.getCustomer();
        Long reservationId = apartment.getReservations().stream()
                .map(Reservation::getId)
                .findAny().orElseThrow(NotFoundException::new);
        Reservation reservation = reservationDbService.gerReservation(reservationId).orElseThrow(NotFoundException::new);
        apartmentDbService.delete(apartmentId);
        customer.getReservations().remove(reservation);
        customer.getApartments().remove(apartment);
        userDbService.update(customer);
        reservationDbService.delete(reservationId);
    }
}
