package com.mateusz.jakuszko.roomforyou.controller;

import com.mateusz.jakuszko.roomforyou.domain.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.facade.ApartmentDbFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/apartments")
public class ApartmentController {

    private final ApartmentDbFacade apartmentDbFacade;

    @GetMapping
    public List<ApartmentDto> get() {
        return apartmentDbFacade.getApartments();
    }

    @GetMapping("/{id}")
    public ApartmentDto get(@PathVariable Long id) {
        return apartmentDbFacade.getApartment(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApartmentDto create(@RequestBody ApartmentDto apartmentDto) {
        return apartmentDbFacade.createApartment(apartmentDto);
    }

    @PutMapping
    public ApartmentDto update(@RequestBody ApartmentDto apartmentDto) {
        return apartmentDbFacade.updateApartment(apartmentDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        apartmentDbFacade.deleteApartment(id);
    }
}
