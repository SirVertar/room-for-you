package com.mateusz.jakuszko.roomforyou.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentDto {
    private Long id;
    private Double latitude;
    private Double longitude;
    private String city;
    private String street;
    private String streetNumber;
    private Integer apartmentNumber;
    private List<Long> reservationsIds;
    private Long customerId;
}
