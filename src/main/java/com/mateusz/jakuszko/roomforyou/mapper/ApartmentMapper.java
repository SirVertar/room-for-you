package com.mateusz.jakuszko.roomforyou.mapper;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApartmentMapper {
    public ApartmentDto mapToApartmentDto(Apartment apartment, List<Long> reservationsIds) {
        return ApartmentDto.builder()
                .id(apartment.getId())
                .city(apartment.getCity())
                .street(apartment.getStreet())
                .streetNumber(apartment.getStreetNumber())
                .apartmentNumber(apartment.getApartmentNumber())
                .latitude(apartment.getLatitude())
                .longitude(apartment.getLongitude())
                .reservationsIds(reservationsIds)
                .userId(apartment.getCustomer().getId())
                .build();
    }

    public Apartment mapToApartment(ApartmentDto apartmentDto, List<Reservation> reservations, Customer customer) {
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
        return apartments.stream()
                .map(apartment -> ApartmentDto.builder()
                        .id(apartment.getId())
                        .city(apartment.getCity())
                        .street(apartment.getStreet())
                        .streetNumber(apartment.getStreetNumber())
                        .apartmentNumber(apartment.getApartmentNumber())
                        .latitude(apartment.getLatitude())
                        .longitude(apartment.getLongitude())
                        .userId(apartment.getCustomer().getId())
                        .reservationsIds(apartment.getReservations().stream()
                                .map(Reservation::getId)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
