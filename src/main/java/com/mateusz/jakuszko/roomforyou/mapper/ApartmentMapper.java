package com.mateusz.jakuszko.roomforyou.mapper;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedApartment;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedCustomer;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedReservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ApartmentMapper {
    public ApartmentDto mapToApartmentDto(Apartment apartment) {
        log.info("Map Apartment to ApartmentDto");
        return ApartmentDto.builder()
                .id(apartment.getId())
                .city(apartment.getCity())
                .street(apartment.getStreet())
                .streetNumber(apartment.getStreetNumber())
                .apartmentNumber(apartment.getApartmentNumber())
                .latitude(apartment.getLatitude())
                .longitude(apartment.getLongitude())
                .reservationsIds(apartment.getReservations().stream()
                .map(Reservation::getId)
                .collect(Collectors.toList()))
                .customerId(apartment.getCustomer().getId())
                .build();
    }

    public Apartment mapToApartment(ApartmentDto apartmentDto, List<Reservation> reservations, Customer customer) {
        log.info("Map ApartmentDto to Apartment");
        return Apartment.builder()
                .id(apartmentDto.getId())
                .city(apartmentDto.getCity())
                .street(apartmentDto.getStreet())
                .streetNumber(apartmentDto.getStreetNumber())
                .apartmentNumber(apartmentDto.getApartmentNumber())
                .latitude(apartmentDto.getLatitude())
                .longitude(apartmentDto.getLongitude())
                .reservations(reservations)
                .customer(customer)
                .build();
    }

    public List<ApartmentDto> mapToApartmentDtos(List<Apartment> apartments) {
        log.info("Map Apartments to ApartmentDtos");
        return apartments.stream()
                .map(apartment -> ApartmentDto.builder()
                        .id(apartment.getId())
                        .city(apartment.getCity())
                        .street(apartment.getStreet())
                        .streetNumber(apartment.getStreetNumber())
                        .apartmentNumber(apartment.getApartmentNumber())
                        .latitude(apartment.getLatitude())
                        .longitude(apartment.getLongitude())
                        .customerId(apartment.getCustomer().getId())
                        .reservationsIds(apartment.getReservations().stream()
                                .map(Reservation::getId)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    public DeletedApartment mapToDeletedApartment(Apartment apartment, DeletedCustomer customer,
            List<DeletedReservation> reservations) {
        return DeletedApartment.builder()
                .id(apartment.getId())
                .city(apartment.getCity())
                .street(apartment.getStreet())
                .streetNumber(apartment.getStreetNumber())
                .apartmentNumber(apartment.getApartmentNumber())
                .latitude(apartment.getLatitude())
                .longitude(apartment.getLongitude())
                .customer(customer)
                .reservations(reservations)
                .build();
    }
}
