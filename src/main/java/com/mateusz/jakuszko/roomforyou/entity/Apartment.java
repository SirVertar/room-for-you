package com.mateusz.jakuszko.roomforyou.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double latitude;
    private Double longitude;
    private String city;
    private String street;
    private String streetNumber;
    private Integer apartmentNumber;
    @OneToMany(mappedBy = "apartment", fetch = FetchType.EAGER)
    private List<Reservation> reservations;
    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;
}
