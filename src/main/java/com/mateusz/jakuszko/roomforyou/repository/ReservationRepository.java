package com.mateusz.jakuszko.roomforyou.repository;

import com.mateusz.jakuszko.roomforyou.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
