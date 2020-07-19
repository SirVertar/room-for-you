package com.mateusz.jakuszko.roomforyou.controller;

import com.mateusz.jakuszko.roomforyou.dto.deleted.DeletedApartmentDto;
import com.mateusz.jakuszko.roomforyou.dto.deleted.DeletedCustomerDto;
import com.mateusz.jakuszko.roomforyou.dto.deleted.DeletedReservationDto;
import com.mateusz.jakuszko.roomforyou.facade.deleted.DeletedDataDbFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/deleted")
public class DeletedDataController {
    private final DeletedDataDbFacade deletedDataDbFacade;

    @GetMapping("/customers")
    public List<DeletedCustomerDto> getCustomers() {
        log.info("Get all deleted customers");
        return deletedDataDbFacade.getCustomers();
    }

    @GetMapping("/reservations")
    public List<DeletedReservationDto> getReservations() {
        log.info("Get all deleted reservations");
        return deletedDataDbFacade.getReservations();
    }

    @GetMapping("/apartments")
    public List<DeletedApartmentDto> getApartments() {
        log.info("Get all deleted apartments");
        return deletedDataDbFacade.getApartments();
    }
}
