package com.mateusz.jakuszko.roomforyou.dto.deleted;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeletedApartmentDto {
    private Long id;
    private Long previousApartmentId;
    private Long previousCustomerId;
    private Double latitude;
    private Double longitude;
    private String city;
    private String street;
    private String streetNumber;
    private Integer apartmentNumber;
    private List<Long> reservationIds;
}
