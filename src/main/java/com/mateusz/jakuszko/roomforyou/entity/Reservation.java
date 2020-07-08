package com.mateusz.jakuszko.roomforyou.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne (fetch = FetchType.EAGER)
    private Apartment apartment;
    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;
}
