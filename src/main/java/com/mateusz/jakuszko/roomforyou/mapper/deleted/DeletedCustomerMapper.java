package com.mateusz.jakuszko.roomforyou.mapper.deleted;

import com.mateusz.jakuszko.roomforyou.dto.deleted.DeletedApartmentDto;
import com.mateusz.jakuszko.roomforyou.dto.deleted.DeletedCustomerDto;
import com.mateusz.jakuszko.roomforyou.dto.deleted.DeletedReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedApartment;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedCustomer;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedReservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DeletedCustomerMapper {
    public DeletedCustomer mapToDeletedCustomer(Customer customer, List<DeletedReservation> reservations,
                                                List<DeletedApartment> apartments) {
        return DeletedCustomer.builder()
                .previousCustomerId(customer.getId())
                .password(customer.getPassword())
                .username(customer.getUsername())
                .name(customer.getName())
                .surname(customer.getSurname())
                .email(customer.getEmail())
                .reservations(reservations)
                .apartments(apartments).build();
    }

    public DeletedCustomerDto mapToDeletedCustomerDto(DeletedCustomer deletedCustomer,
                                                      List<DeletedApartmentDto> apartments,
                                                      List<DeletedReservationDto> reservations) {
        return DeletedCustomerDto.builder()
                .id(deletedCustomer.getId())
                .previousCustomerId(deletedCustomer.getPreviousCustomerId())
                .username(deletedCustomer.getUsername())
                .name(deletedCustomer.getName())
                .surname(deletedCustomer.getSurname())
                .email(deletedCustomer.getEmail())
                .password(deletedCustomer.getPassword())
                .apartments(apartments)
                .reservations(reservations).build();
    }

    public List<DeletedCustomerDto> mapToDeletedCustomerDtos(List<DeletedCustomer> deletedCustomers,
                                                             List<DeletedApartmentDto> apartments,
                                                             List<DeletedReservationDto> reservations) {
        return deletedCustomers.stream()
                .map(customer -> DeletedCustomerDto.builder()
                        .id(customer.getId())
                        .previousCustomerId(customer.getPreviousCustomerId())
                        .username(customer.getUsername())
                        .name(customer.getName())
                        .surname(customer.getSurname())
                        .email(customer.getEmail())
                        .password(customer.getPassword())
                        .apartments(apartments.stream()
                        .filter(deletedApartmentDto -> deletedApartmentDto
                                .getPreviousCustomerId().equals(customer.getPreviousCustomerId()))
                        .collect(Collectors.toList()))
                        .reservations(reservations.stream()
                        .filter(deletedReservationDto -> deletedReservationDto
                                .getPreviousCustomerId().equals(customer.getPreviousCustomerId()))
                        .collect(Collectors.toList())).build())
                .collect(Collectors.toList());
    }

}
