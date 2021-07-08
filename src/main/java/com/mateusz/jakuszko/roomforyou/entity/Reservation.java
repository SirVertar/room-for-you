package com.mateusz.jakuszko.roomforyou.entity;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne(fetch = FetchType.EAGER)
    private Apartment apartment;
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;
}
