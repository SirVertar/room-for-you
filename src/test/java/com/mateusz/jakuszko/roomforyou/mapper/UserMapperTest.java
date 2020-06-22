package com.mateusz.jakuszko.roomforyou.mapper;

import com.mateusz.jakuszko.roomforyou.domain.*;
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
public class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    private User createUser() {
        return User.builder()
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
        User user = createUser();
        UserDto expectedUserDto = createUserDto();
        //When
        UserDto userDto = userMapper.mapToUserDto(user, Arrays.asList(ApartmentDto.builder().id(11L).build()),
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
        User expectedUser = createUser();
        //When
        User user = userMapper.mapToUser(userDto, Arrays.asList(Apartment.builder().id(11L).build()),
                Arrays.asList(Reservation.builder().id(12L).build()));
        //Then
        assertEquals(expectedUser.getId(), user.getId());
        assertEquals(expectedUser.getName(), user.getName());
        assertEquals(expectedUser.getSurname(), user.getSurname());
        assertEquals(expectedUser.getUsername(), user.getUsername());
        assertEquals(expectedUser.getPassword(), user.getPassword());
        assertEquals(expectedUser.getEmail(), user.getEmail());
        assertEquals(expectedUser.getApartments().size(), user.getApartments().size());
        assertEquals(expectedUser.getApartments().get(0).getId(), user.getApartments().get(0).getId());
        assertEquals(expectedUser.getReservations().size(), user.getReservations().size());
        assertEquals(expectedUser.getReservations().get(0).getId(), user.getReservations().get(0).getId());
    }

    @Test
    public void mapToUserDtosTest() {
        //Given
        User user = createUser();
        List<User> users = new ArrayList<>();
        users.add(user);
        //When
        List<UserDto> userDtos = userMapper.mapToUserDtos(users, Arrays.asList(ApartmentDto.builder().id(11L).userId(1L).build()),
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
