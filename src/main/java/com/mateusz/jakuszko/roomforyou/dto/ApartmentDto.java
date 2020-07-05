package com.mateusz.jakuszko.roomforyou.dto;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ApartmentDto {
    private Long id;
    private Long latitude;
    private Long longitude;
    private String city;
    private String street;
    private String streetNumber;
    private Integer apartmentNumber;
    private List<Long> reservationsIds;
    private Long userId;
}
