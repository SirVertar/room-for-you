package com.mateusz.jakuszko.roomforyou.domain;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String role;
    private List<ReservationDto> reservations;
    private List<ApartmentDto> apartments;
}
