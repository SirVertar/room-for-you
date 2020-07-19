package com.mateusz.jakuszko.roomforyou.facade.deleted;

import com.mateusz.jakuszko.roomforyou.dto.deleted.DeletedApartmentDto;
import com.mateusz.jakuszko.roomforyou.dto.deleted.DeletedCustomerDto;
import com.mateusz.jakuszko.roomforyou.dto.deleted.DeletedReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedApartment;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedCustomer;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedReservation;
import com.mateusz.jakuszko.roomforyou.mapper.deleted.DeletedApartmentMapper;
import com.mateusz.jakuszko.roomforyou.mapper.deleted.DeletedCustomerMapper;
import com.mateusz.jakuszko.roomforyou.mapper.deleted.DeletedReservationMapper;
import com.mateusz.jakuszko.roomforyou.service.deleted.DeletedApartmentDbService;
import com.mateusz.jakuszko.roomforyou.service.deleted.DeletedCustomerDbService;
import com.mateusz.jakuszko.roomforyou.service.deleted.DeletedReservationDbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeletedDataDbFacade {
    private final DeletedCustomerDbService customerDbService;
    private final DeletedCustomerMapper customerMapper;
    private final DeletedApartmentDbService apartmentDbService;
    private final DeletedApartmentMapper apartmentMapper;
    private final DeletedReservationDbService reservationDbService;
    private final DeletedReservationMapper reservationMapper;

    @Transactional
    public List<DeletedCustomerDto> getCustomers() {
        log.info("Get all DeletedCustomers");
        List<DeletedCustomer> customers = customerDbService.getUsers();
        List<DeletedReservation> reservations = reservationDbService.getReservations();
        List<DeletedApartment> apartments = apartmentDbService.getApartments();
        List<DeletedApartmentDto> apartmentDtos = apartmentMapper.mapToDeletedApartmentDtos(apartments);
        List<DeletedReservationDto> reservationDtos = reservationMapper.mapToDeletedReservationDtos(reservations);
        return customerMapper.mapToDeletedCustomerDtos(customers, apartmentDtos, reservationDtos);
    }

    @Transactional
    public List<DeletedReservationDto> getReservations() {
        log.info("Get all DeletedReservations");
        List<DeletedReservation> reservations = reservationDbService.getReservations();
        return reservationMapper.mapToDeletedReservationDtos(reservations);
    }

    @Transactional
    public List<DeletedApartmentDto> getApartments() {
        log.info("Get all apartments");
        List<DeletedApartment> apartments = apartmentDbService.getApartments();
        List<DeletedReservation> reservations = reservationDbService.getReservations();
        return apartmentMapper.mapToDeletedApartmentDtos(apartments);
    }
}
