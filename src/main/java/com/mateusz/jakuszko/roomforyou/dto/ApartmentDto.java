package com.mateusz.jakuszko.roomforyou.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
