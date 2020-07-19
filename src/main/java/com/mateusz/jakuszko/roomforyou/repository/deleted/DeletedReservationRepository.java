package com.mateusz.jakuszko.roomforyou.repository.deleted;

import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface DeletedReservationRepository extends JpaRepository<DeletedReservation, Long> {
}
