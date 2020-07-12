package com.mateusz.jakuszko.roomforyou.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {
    private java.lang.Long id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String role;
    private List<ReservationDto> reservations;
    private List<ApartmentDto> apartmentDtos;
}
