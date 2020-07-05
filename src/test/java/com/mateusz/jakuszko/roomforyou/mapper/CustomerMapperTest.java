package com.mateusz.jakuszko.roomforyou.mapper;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.dto.ReservationDto;
import com.mateusz.jakuszko.roomforyou.dto.UserDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
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
    UserMapper userMapper;

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

    private UserDto createUserDto() {
        return UserDto.builder()
                .id(1L)
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .email("mateusz.jakuszko@gmail.com")
                .apartments(Arrays.asList(ApartmentDto.builder().id(11L).userId(1L).build()))
                .reservations(Arrays.asList(ReservationDto.builder().id(12L).userId(1L).build()))
                .build();
    }

    @Test
    public void mapToUserDtoTest() {
        //Given
        Customer customer = createUser();
        UserDto expectedUserDto = createUserDto();
        //When
        UserDto userDto = userMapper.mapToUserDto(customer, Arrays.asList(ApartmentDto.builder().id(11L).build()),
                Arrays.asList(ReservationDto.builder().id(12L).build()));
        //Then
        assertEquals(expectedUserDto.getId(), userDto.getId());
        assertEquals(expectedUserDto.getName(), userDto.getName());
        assertEquals(expectedUserDto.getSurname(), userDto.getSurname());
        assertEquals(expectedUserDto.getUsername(), userDto.getUsername());
        assertEquals(expectedUserDto.getPassword(), userDto.getPassword());
        assertEquals(expectedUserDto.getEmail(), userDto.getEmail());
        assertEquals(expectedUserDto.getApartments().size(), userDto.getApartments().size());
        assertEquals(expectedUserDto.getApartments().get(0).getId(), userDto.getApartments().get(0).getId());
        assertEquals(expectedUserDto.getReservations().size(), userDto.getReservations().size());
        assertEquals(expectedUserDto.getReservations().get(0).getId(), userDto.getReservations().get(0).getId());
    }

    @Test
    public void mapToUserTest() {
        //Given
        UserDto userDto = createUserDto();
        Customer expectedCustomer = createUser();
        //When
        Customer customer = userMapper.mapToUser(userDto, Arrays.asList(Apartment.builder().id(11L).build()),
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
    public void mapToUserDtosTest() {
        //Given
        Customer customer = createUser();
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        //When
        List<UserDto> userDtos = userMapper.mapToUserDtos(customers, Arrays.asList(ApartmentDto.builder().id(11L).userId(1L).build()),
                Arrays.asList(ReservationDto.builder().id(12L).userId(1L).build()));
        //Then
        assertEquals(1, userDtos.size());
        assertEquals(1L, userDtos.get(0).getId().longValue());
        assertEquals("Mateusz", userDtos.get(0).getName());
        assertEquals("Jakuszko", userDtos.get(0).getSurname());
        assertEquals("matanos", userDtos.get(0).getUsername());
        assertEquals("abc123", userDtos.get(0).getPassword());
        assertEquals("mateusz.jakuszko@gmail.com", userDtos.get(0).getEmail());
        assertEquals(12L, userDtos.get(0).getReservations().get(0).getId().longValue());
        assertEquals(11L, userDtos.get(0).getApartments().get(0).getId().longValue());
    }
}
