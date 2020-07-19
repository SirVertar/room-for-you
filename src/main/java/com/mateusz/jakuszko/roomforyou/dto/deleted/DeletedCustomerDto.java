package com.mateusz.jakuszko.roomforyou.dto.deleted;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeletedCustomerDto {
    private Long id;
    private Long previousCustomerId;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String role;
    private List<DeletedReservationDto> reservations = new ArrayList<>();
    private List<DeletedApartmentDto> apartments = new ArrayList<>();
}
