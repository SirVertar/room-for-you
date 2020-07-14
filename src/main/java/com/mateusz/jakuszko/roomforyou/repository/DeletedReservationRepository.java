package com.mateusz.jakuszko.roomforyou.repository;

import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedReservationRepository extends JpaRepository<DeletedReservation, Long> {
}
