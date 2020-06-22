package com.mateusz.jakuszko.roomforyou.mapper;

import com.mateusz.jakuszko.roomforyou.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApartmentMapper {
    public ApartmentDto mapToApartmentDto(Apartment apartment, List<Long> reservationsIds) {
        return ApartmentDto.builder()
                .id(apartment.getId())
                .street(apartment.getStreet())
                .streetNumber(apartment.getStreetNumber())
                .apartmentNumber(apartment.getApartmentNumber())
                .xCoordinate(apartment.getXCoordinate())
                .yCoordinate(apartment.getYCoordinate())
                .reservationsIds(reservationsIds)
                .userId(apartment.getUser().getId())
                .build();
    }

    public Apartment mapToApartment(ApartmentDto apartmentDto, List<Reservation> reservations, User user) {
        return Apartment.builder()
                .id(apartmentDto.getId())
                .street(apartmentDto.getStreet())
                .streetNumber(apartmentDto.getStreetNumber())
                .apartmentNumber(apartmentDto.getApartmentNumber())
                .xCoordinate(apartmentDto.getXCoordinate())
                .yCoordinate(apartmentDto.getYCoordinate())
                .reservations(reservations)
                .user(user)
                .build();
    }

    public List<ApartmentDto> mapToApartmentDtos(List<Apartment> apartments) {
        return apartments.stream()
                .map(apartment -> ApartmentDto.builder()
                        .id(apartment.getId())
                        .street(apartment.getStreet())
                        .streetNumber(apartment.getStreetNumber())
                        .apartmentNumber(apartment.getApartmentNumber())
                        .xCoordinate(apartment.getXCoordinate())
                        .yCoordinate(apartment.getYCoordinate())
                        .userId(apartment.getUser().getId())
                        .reservationsIds(apartment.getReservations().stream()
                                .map(Reservation::getId)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
