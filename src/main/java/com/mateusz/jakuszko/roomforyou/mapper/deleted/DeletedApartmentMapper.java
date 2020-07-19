package com.mateusz.jakuszko.roomforyou.mapper.deleted;

import com.mateusz.jakuszko.roomforyou.dto.deleted.DeletedApartmentDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedApartment;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedReservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DeletedApartmentMapper {

    public DeletedApartment mapToDeletedApartment(Apartment apartment, List<DeletedReservation> reservations) {
        return DeletedApartment.builder()
                .previousApartmentId(apartment.getId())
                .city(apartment.getCity())
                .street(apartment.getStreet())
                .streetNumber(apartment.getStreetNumber())
                .apartmentNumber(apartment.getApartmentNumber())
                .previousCustomerId(apartment.getCustomer().getId())
                .reservations(reservations).build();
    }

    public List<DeletedApartment> mapToDeleteApartments(List<Apartment> apartments, List<DeletedReservation> reservations) {
        return apartments.stream()
                .map(apartment -> DeletedApartment.builder()
                        .previousApartmentId(apartment.getId())
                        .city(apartment.getCity())
                        .street(apartment.getStreet())
                        .streetNumber(apartment.getStreetNumber())
                        .apartmentNumber(apartment.getApartmentNumber())
                        .previousCustomerId(apartment.getCustomer().getId())
                        .reservations(reservations.stream()
                                .filter(reservation -> reservation.getPreviousApartmentId().equals(apartment.getId()))
                                .collect(Collectors.toList())).build())
                .collect(Collectors.toList());
    }

    public List<DeletedApartment> mapToDeleteApartments(List<Apartment> apartments) {
        return apartments.stream()
                .map(apartment -> DeletedApartment.builder()
                        .previousApartmentId(apartment.getId())
                        .city(apartment.getCity())
                        .street(apartment.getStreet())
                        .streetNumber(apartment.getStreetNumber())
                        .apartmentNumber(apartment.getApartmentNumber())
                        .previousCustomerId(apartment.getCustomer().getId())
                        .reservations(new ArrayList<>()).build())
                .collect(Collectors.toList());
    }

    public DeletedApartmentDto mapToDeletedApartmentDto(DeletedApartment apartment) {
        return DeletedApartmentDto.builder()
                .id(apartment.getId())
                .previousApartmentId(apartment.getPreviousApartmentId())
                .previousCustomerId(apartment.getPreviousCustomerId())
                .city(apartment.getCity())
                .street(apartment.getStreet())
                .streetNumber(apartment.getStreetNumber())
                .apartmentNumber(apartment.getApartmentNumber())
                .latitude(apartment.getLatitude())
                .longitude(apartment.getLongitude())
                .reservationIds(apartment.getReservations().stream()
                .map(DeletedReservation::getId)
                .collect(Collectors.toList())).build();
    }

    public List<DeletedApartmentDto> mapToDeletedApartmentDtos(List<DeletedApartment> apartments) {
        return apartments.stream()
                .map(apartment -> DeletedApartmentDto.builder()
                        .id(apartment.getId())
                        .previousApartmentId(apartment.getPreviousApartmentId())
                        .previousCustomerId(apartment.getPreviousCustomerId())
                        .city(apartment.getCity())
                        .street(apartment.getStreet())
                        .streetNumber(apartment.getStreetNumber())
                        .apartmentNumber(apartment.getApartmentNumber())
                        .latitude(apartment.getLatitude())
                        .longitude(apartment.getLongitude())
                        .reservationIds(apartment.getReservations().stream()
                                .map(DeletedReservation::getId)
                                .collect(Collectors.toList())).build())
                .collect(Collectors.toList());
    }

}
