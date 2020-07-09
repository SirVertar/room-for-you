package com.mateusz.jakuszko.roomforyou.facade;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import com.mateusz.jakuszko.roomforyou.mapper.ApartmentMapper;
import com.mateusz.jakuszko.roomforyou.opencagegeocoder.OpenCageGeocoderClient;
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService;
import com.mateusz.jakuszko.roomforyou.service.CustomerDbService;
import com.mateusz.jakuszko.roomforyou.service.ReservationDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ApartmentDbFacade {

    private final CustomerDbService customerDbService;
    private final ApartmentDbService apartmentDbService;
    private final ApartmentMapper apartmentMapper;
    private final ReservationDbService reservationDbService;
    private final OpenCageGeocoderClient openCageGeocoderClient;

    @Transactional
    public ApartmentDto getApartment(Long apartmentId) {
        log.info("Get apartment by id - " + apartmentId);
        Apartment apartment = apartmentDbService.getApartment(apartmentId).orElseThrow(NotFoundException::new);
        List<Long> reservationsIds = apartment.getReservations().stream()
                .map(Reservation::getId)
                .collect(Collectors.toList());
        return apartmentMapper.mapToApartmentDto(apartment);
    }

    @Transactional
    public List<ApartmentDto> getApartments() {
        log.info("Get all apartments");
        List<Apartment> apartments = apartmentDbService.getApartments();
        List<Reservation> reservations = reservationDbService.getReservations();
        return apartmentMapper.mapToApartmentDtos(apartments);
    }

    @Transactional
    public ApartmentDto createApartment(ApartmentDto apartmentDto) throws IOException, ParseException {
        log.info("Create Apartment");
        List<Reservation> reservations = reservationDbService.getReservationsByApartmentId(apartmentDto.getId());
        Customer customer = customerDbService.getUser(apartmentDto.getUserId()).orElseThrow(NotFoundException::new);
        Map<String, String> map =  openCageGeocoderClient.getGeometryValues(apartmentDto);
        Double latitude = Double.parseDouble(map.get("latitude"));
        Double longitude =Double.parseDouble(map.get("longitude"));
        apartmentDto.setLatitude(latitude);
        apartmentDto.setLongitude(longitude);
        Apartment apartment = apartmentMapper.mapToApartment(apartmentDto, reservations, customer);
        List<Apartment> newUserApartmentList = customerDbService.getUser(customer.getId()).orElseThrow(NotFoundException::new).getApartments();
        apartmentDbService.save(apartment);
        newUserApartmentList.add(apartment);
        customer.setApartments(newUserApartmentList);
        customerDbService.update(customer);
        return apartmentMapper.mapToApartmentDto(apartment);
    }

    public ApartmentDto updateApartment(ApartmentDto apartmentDto) throws IOException, ParseException {
        return createApartment(apartmentDto);
    }

    // TODO Add functionality - After delete apartment and reservations from user -> save in other entity
    @Transactional
    public void deleteApartment(Long apartmentId) {
        log.info("Delete apartment by id - " + apartmentId);
        Apartment apartment = apartmentDbService.getApartment(apartmentId).orElseThrow(NotFoundException::new);
        Customer customer = apartment.getCustomer();
        Optional<Long> reservationId = apartment.getReservations().stream()
                .map(Reservation::getId)
                .findAny();
        if (reservationId.isPresent()) {
            Reservation reservation = reservationDbService.gerReservation(reservationId.get()).orElseThrow(NotFoundException::new);
            apartmentDbService.delete(apartmentId);
            customer.getReservations().remove(reservation);
            customer.getApartments().remove(apartment);
            customerDbService.update(customer);
            reservationDbService.delete(reservationId.get());
        } else {
            apartmentDbService.delete(apartmentId);
            customer.getApartments().remove(apartment);
            customerDbService.update(customer);
        }
    }
}
