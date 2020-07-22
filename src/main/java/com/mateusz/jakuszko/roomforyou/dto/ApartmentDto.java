package com.mateusz.jakuszko.roomforyou.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
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

    private ApartmentDto(Long id, Double latitude, Double longitude, String city, String street,
                         String streetNumber, Integer apartmentNumber, List<Long> reservationsIds,
                         Long customerId) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
        this.apartmentNumber = apartmentNumber;
        this.reservationsIds = reservationsIds;
        this.customerId = customerId;
    }

    public static class Builder {

        private Long id;
        private Double latitude;
        private Double longitude;
        private String city;
        private String street;
        private String streetNumber;
        private Integer apartmentNumber;
        private List<Long> reservationsIds;
        private Long customerId;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder latitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder streetNumber(String streetNumber) {
            this.streetNumber = streetNumber;
            return this;
        }

        public Builder apartmentNumber(Integer apartmentNumber) {
            this.apartmentNumber = apartmentNumber;
            return this;
        }

        public Builder reservationsIds(List<Long> reservationsIds) {
            this.reservationsIds = reservationsIds;
            return this;
        }

        public Builder customerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public ApartmentDto build() {
            return new ApartmentDto(id, latitude, longitude, city, street, streetNumber, apartmentNumber,
                    reservationsIds, customerId);
        }
    }


}
