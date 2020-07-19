package com.mateusz.jakuszko.roomforyou.entity.deleted;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class DeletedReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long previousReservationId;
    private Long previousCustomerId;
    private Long previousApartmentId;
    private LocalDate startDate;
    private LocalDate endDate;
}
