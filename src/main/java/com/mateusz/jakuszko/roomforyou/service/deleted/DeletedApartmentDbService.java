package com.mateusz.jakuszko.roomforyou.service.deleted;

import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedApartment;
import com.mateusz.jakuszko.roomforyou.repository.deleted.DeletedApartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeletedApartmentDbService {
    private final DeletedApartmentRepository apartmentRepository;

    public Optional<DeletedApartment> getApartment(Long id) {
        return apartmentRepository.findById(id);
    }

    public List<DeletedApartment> getApartments() {
        return apartmentRepository.findAll();
    }

    public DeletedApartment save(DeletedApartment apartment) {
        return apartmentRepository.save(apartment);
    }

    public DeletedApartment update(DeletedApartment apartment) {
        return apartmentRepository.save(apartment);
    }

    public void delete(Long id) {
        apartmentRepository.deleteById(id);
    }
}
