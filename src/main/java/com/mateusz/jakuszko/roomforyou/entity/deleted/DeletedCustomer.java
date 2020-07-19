package com.mateusz.jakuszko.roomforyou.entity.deleted;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class DeletedCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long previousCustomerId;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String role;
    @Builder.Default
    @OneToMany
    private List<DeletedReservation> reservations = new ArrayList<>();
    @Builder.Default
    @OneToMany
    private List<DeletedApartment> apartments = new ArrayList<>();
}
