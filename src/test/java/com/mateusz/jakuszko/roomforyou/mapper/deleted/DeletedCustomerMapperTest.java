package com.mateusz.jakuszko.roomforyou.mapper.deleted;

import com.mateusz.jakuszko.roomforyou.dto.deleted.DeletedApartmentDto;
import com.mateusz.jakuszko.roomforyou.dto.deleted.DeletedCustomerDto;
import com.mateusz.jakuszko.roomforyou.dto.deleted.DeletedReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedApartment;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedCustomer;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedReservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DeletedCustomerMapperTest {

    @Autowired
    private DeletedCustomerMapper deletedCustomerMapper;

    private Customer createCustomer() {
        return Customer.builder()
                .id(1L)
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .email("mateusz.jakuszko@gmail.com")
                .apartments(Collections.singletonList(Apartment.builder().id(11L).build()))
                .reservations(Collections.singletonList(Reservation.builder().id(12L).build()))
                .build();
    }

    private DeletedCustomer createDeletedCustomer() {
        return DeletedCustomer.builder()
                .id(33L)
                .previousCustomerId(10L)
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .email("mateusz.jakuszko@gmail.com")
                .apartments(Collections.singletonList(DeletedApartment.builder()
                        .previousApartmentId(11L)
                        .previousCustomerId(10L).build()))
                .reservations(Collections.singletonList(DeletedReservation.builder()
                        .previousReservationId(12L)
                        .previousCustomerId(10L).build()))
                .build();
    }

    @Test
    public void shouldMapCustomerToDeletedCustomer() {
        //Given
        Customer customer = createCustomer();
        //When
        DeletedCustomer deletedCustomer = deletedCustomerMapper
                .mapToDeletedCustomer(customer,
                        Collections.singletonList(DeletedReservation.builder().previousReservationId(12L).build()),
                        Collections.singletonList(DeletedApartment.builder().previousApartmentId(11L).build()));
        //Then
        assertEquals(customer.getId(), deletedCustomer.getPreviousCustomerId());
        assertEquals(customer.getApartments().get(0).getId(), deletedCustomer.getApartments().get(0).getPreviousApartmentId());
        assertEquals(customer.getReservations().get(0).getId(), deletedCustomer.getReservations().get(0).getPreviousReservationId());
        assertEquals(customer.getName(), deletedCustomer.getName());
        assertEquals(customer.getSurname(), deletedCustomer.getSurname());
        assertEquals(customer.getUsername(), deletedCustomer.getUsername());
        assertEquals(customer.getEmail(), deletedCustomer.getEmail());
        assertEquals(customer.getPassword(), deletedCustomer.getPassword());
        assertEquals(customer.getRole(), deletedCustomer.getRole());
    }

    @Test
    public void mapToDeletedCustomerDtos() {
        //Given
        List<DeletedCustomer> customers = Collections.singletonList(createDeletedCustomer());
        //When
        List<DeletedCustomerDto> deletedCustomers = deletedCustomerMapper
                .mapToDeletedCustomerDtos(customers,
                        Collections.singletonList(DeletedApartmentDto.builder().previousApartmentId(11L)
                                .previousCustomerId(10L).build()),
                        Collections.singletonList(DeletedReservationDto.builder().previousReservationId(12L)
                                .previousCustomerId(10L).build()));
        //Then
        assertEquals(customers.get(0).getPreviousCustomerId(), deletedCustomers.get(0).getPreviousCustomerId());
        assertEquals(customers.get(0).getApartments().get(0).getPreviousApartmentId(), deletedCustomers.get(0).getApartments().get(0).getPreviousApartmentId());
        assertEquals(customers.get(0).getReservations().get(0).getPreviousReservationId(), deletedCustomers.get(0).getReservations().get(0).getPreviousReservationId());
        assertEquals(customers.get(0).getName(), deletedCustomers.get(0).getName());
        assertEquals(customers.get(0).getSurname(), deletedCustomers.get(0).getSurname());
        assertEquals(customers.get(0).getUsername(), deletedCustomers.get(0).getUsername());
        assertEquals(customers.get(0).getEmail(), deletedCustomers.get(0).getEmail());
        assertEquals(customers.get(0).getPassword(), deletedCustomers.get(0).getPassword());
        assertEquals(customers.get(0).getRole(), deletedCustomers.get(0).getRole());
    }
}
