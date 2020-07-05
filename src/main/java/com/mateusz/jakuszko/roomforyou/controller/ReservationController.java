package com.mateusz.jakuszko.roomforyou.controller;


import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.facade.ReservationDbFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/reservations")
public class ReservationController {

    private final ReservationDbFacade reservationDbFacade;

    @GetMapping
    public List<ReservationDto> get() {
        return reservationDbFacade.getReservations();
    }

    @GetMapping("/{id}")
    public ReservationDto get(@PathVariable Long id) {
        return reservationDbFacade.getReservation(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReservationDto create(@RequestBody ReservationDto reservationDto) {
        return reservationDbFacade.createReservation(reservationDto);
    }

    @PutMapping
    public ReservationDto update(@RequestBody ReservationDto reservationDto){
        return reservationDbFacade.updateReservation(reservationDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reservationDbFacade.deleteReservation(id);
    }
}
