package com.mateusz.jakuszko.roomforyou.mapper;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.dto.CustomerDto;
import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerMapperTest {

    @Autowired
    CustomerMapper customerMapper;

    private Customer createUser() {
        return Customer.builder()
                .id(1L)
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .email("mateusz.jakuszko@gmail.com")
                .apartments(Arrays.asList(Apartment.builder().id(11L).build()))
                .reservations(Arrays.asList(Reservation.builder().id(12L).build()))
                .build();
    }

    private CustomerDto createUserDto() {
        return CustomerDto.builder()
                .id(1L)
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .email("mateusz.jakuszko@gmail.com")
                .apartments(Arrays.asList(new ApartmentDto.Builder().id(11L).customerId(1L).build()))
                .reservations(Arrays.asList(ReservationDto.builder().id(12L).customerId(1L).build()))
                .build();
    }

    @Test
    public void whenMapCustomerToCustomerDtoShouldReturnExactCustomerDtoObject() {
        //Given
        Customer customer = createUser();
        CustomerDto expectedCustomerDto = createUserDto();
        //When
        CustomerDto customerDto = customerMapper.mapToCustomerDto(customer, Arrays.asList(new ApartmentDto.Builder().id(11L).build()),
                Arrays.asList(ReservationDto.builder().id(12L).build()));
        //Then
        assertEquals(expectedCustomerDto.getId(), customerDto.getId());
        assertEquals(expectedCustomerDto.getName(), customerDto.getName());
        assertEquals(expectedCustomerDto.getSurname(), customerDto.getSurname());
        assertEquals(expectedCustomerDto.getUsername(), customerDto.getUsername());
        assertEquals(expectedCustomerDto.getPassword(), customerDto.getPassword());
        assertEquals(expectedCustomerDto.getEmail(), customerDto.getEmail());
        assertEquals(expectedCustomerDto.getApartments().size(), customerDto.getApartments().size());
        assertEquals(expectedCustomerDto.getApartments().get(0).getId(), customerDto.getApartments().get(0).getId());
        assertEquals(expectedCustomerDto.getReservations().size(), customerDto.getReservations().size());
        assertEquals(expectedCustomerDto.getReservations().get(0).getId(), customerDto.getReservations().get(0).getId());
    }

    @Test
    public void whenMapCustomerDtoToCustomerShouldReturnExactCustomerObject() {
        //Given
        CustomerDto customerDto = createUserDto();
        Customer expectedCustomer = createUser();
        //When
        Customer customer = customerMapper.mapToCustomer(customerDto, Arrays.asList(Apartment.builder().id(11L).build()),
                Arrays.asList(Reservation.builder().id(12L).build()));
        //Then
        assertEquals(expectedCustomer.getId(), customer.getId());
        assertEquals(expectedCustomer.getName(), customer.getName());
        assertEquals(expectedCustomer.getSurname(), customer.getSurname());
        assertEquals(expectedCustomer.getUsername(), customer.getUsername());
        assertEquals(expectedCustomer.getPassword(), customer.getPassword());
        assertEquals(expectedCustomer.getEmail(), customer.getEmail());
        assertEquals(expectedCustomer.getApartments().size(), customer.getApartments().size());
        assertEquals(expectedCustomer.getApartments().get(0).getId(), customer.getApartments().get(0).getId());
        assertEquals(expectedCustomer.getReservations().size(), customer.getReservations().size());
        assertEquals(expectedCustomer.getReservations().get(0).getId(), customer.getReservations().get(0).getId());
    }

    @Test
    public void whenMapCustomersToCustomerDtosShouldReturnExactListOfCustomerDto() {
        //Given
        Customer customer = createUser();
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        //When
        List<CustomerDto> customerDtos = customerMapper.mapToCustomerDtos(customers,
                Arrays.asList(new ApartmentDto.Builder().id(11L).customerId(1L).build()),
                Arrays.asList(ReservationDto.builder().id(12L).customerId(1L).build()));
        //Then
        assertEquals(1, customerDtos.size());
        assertEquals(1L, customerDtos.get(0).getId().longValue());
        assertEquals("Mateusz", customerDtos.get(0).getName());
        assertEquals("Jakuszko", customerDtos.get(0).getSurname());
        assertEquals("matanos", customerDtos.get(0).getUsername());
        assertEquals("abc123", customerDtos.get(0).getPassword());
        assertEquals("mateusz.jakuszko@gmail.com", customerDtos.get(0).getEmail());
        assertEquals(12L, customerDtos.get(0).getReservations().get(0).getId().longValue());
        assertEquals(11L, customerDtos.get(0).getApartments().get(0).getId().longValue());
    }
}
