package com.mateusz.jakuszko.roomforyou.service;

import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.repository.ApartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApartmentDbService {

    private final ApartmentRepository apartmentRepository;

    public ApartmentDbService(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    public Optional<Apartment> getApartment(Long id) {
        return apartmentRepository.findById(id);
    }

    public List<Apartment> getApartments() {
        return apartmentRepository.findAll();
    }

    public Apartment save(Apartment apartment) {
        return apartmentRepository.save(apartment);
    }

    public Apartment update(Apartment apartment) {
        return apartmentRepository.save(apartment);
    }

    public void delete(Long id) {
        apartmentRepository.deleteById(id);
    }

    public List<Apartment> findByUserId(Long userId) {
        return getApartments().stream()
                .filter(apartment -> apartment.getCustomer().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public void deleteApartmentsByUserId(Long userId) {
        getApartments().stream()
                .filter(apartment -> apartment.getCustomer().getId().equals(userId))
                .map(Apartment::getId)
                .forEach(this::delete);
    }
}
