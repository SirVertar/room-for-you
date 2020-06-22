package com.mateusz.jakuszko.roomforyou.domain;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ApartmentDto {

    private Long id;
    private Long xCoordinate;
    private Long yCoordinate;
    private String street;
    private Integer streetNumber;
    private Integer apartmentNumber;
    private List<Long> reservationsIds;
    private Long userId;
}
