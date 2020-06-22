package com.mateusz.jakuszko.roomforyou.domain;

import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationDto {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private ApartmentDto apartmentDto;
    private Long userId;
}
