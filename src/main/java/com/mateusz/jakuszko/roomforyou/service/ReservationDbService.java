package com.mateusz.jakuszko.roomforyou.service;

import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationDbService {
    private final ReservationRepository reservationRepository;

    public ReservationDbService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Optional<Reservation> gerReservation(Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> getReservations() {
        return reservationRepository.findAll();
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation update(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> getReservationsByUserId(Long userId) {
        return getReservations().stream()
                .filter(reservation -> reservation.getCustomer().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Reservation> getReservationsByApartmentId(Long apartmentId) {
        return getReservations().stream()
                .filter(reservation -> reservation.getApartment().getId().equals(apartmentId))
                .collect(Collectors.toList());
    }
}
