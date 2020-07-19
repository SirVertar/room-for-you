package com.mateusz.jakuszko.roomforyou.mapper.deleted;

import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedApartment;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedCustomer;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedReservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

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

}
