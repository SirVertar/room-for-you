package com.mateusz.jakuszko.roomforyou.controller;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.facade.ApartmentDbFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/apartments")
public class ApartmentController {

    private final ApartmentDbFacade apartmentDbFacade;

    @GetMapping
    public List<ApartmentDto> get() {
        log.info("Get all apartments");
        return apartmentDbFacade.getApartments();
    }

    @GetMapping("/{id}")
    public ApartmentDto get(@PathVariable Long id) {
        log.info("Get apartment by id - " + id);
        return apartmentDbFacade.getApartment(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ApartmentDto create(@RequestBody ApartmentDto apartmentDto) throws IOException, ParseException {
        log.info("Create apartment");
        return apartmentDbFacade.createApartment(apartmentDto);
    }

    @PutMapping
    public ApartmentDto update(@RequestBody ApartmentDto apartmentDto) throws IOException, ParseException {
        log.info("Update apartment");
        return apartmentDbFacade.updateApartment(apartmentDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Delete apartment, id - " + id );
        apartmentDbFacade.deleteApartment(id);
    }
}
