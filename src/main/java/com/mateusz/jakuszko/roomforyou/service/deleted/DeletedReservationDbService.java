package com.mateusz.jakuszko.roomforyou.service.deleted;

import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedReservation;
import com.mateusz.jakuszko.roomforyou.repository.deleted.DeletedReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeletedReservationDbService {
    private final DeletedReservationRepository reservationRepository;

    public Optional<DeletedReservation> gerReservation(Long id) {
        return reservationRepository.findById(id);
    }

    public List<DeletedReservation> getReservations() {
        return reservationRepository.findAll();
    }

    public DeletedReservation save(DeletedReservation reservation) {
        return reservationRepository.save(reservation);
    }

    public DeletedReservation update(DeletedReservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void delete(Long id) {
        reservationRepository.deleteById(id);
    }
}
