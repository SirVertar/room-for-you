package com.mateusz.jakuszko.roomforyou.domain;

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
    private Long xCoordinate;
    private Long yCoordinate;
    private String street;
    private Integer streetNumber;
    private Integer apartmentNumber;
    @OneToMany
    private List<Reservation> reservations;
    @ManyToOne
    private User user;
}
