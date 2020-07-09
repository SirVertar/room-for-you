package com.mateusz.jakuszko.roomforyou.facade.searcher;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService;
import com.mateusz.jakuszko.roomforyou.service.CustomerDbService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SearcherDbFacadeTest {

    @Autowired
    private CustomerDbService customerDbService;
    @Autowired
    private ApartmentDbService apartmentDbService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SearcherDbFacade searcherDbFacade;

    @Test
    public void whenSearchApartmentByCityAndStreetShouldReturnCorrectApartmentFroDb() {
        //Given
        Customer customer = Customer.builder()
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .role("Admin")
                .email("mateusz.jakuszko@gmail.com")
                .build();
        Apartment apartment1 = Apartment.builder()
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("26")
                .apartmentNumber(5)
                .latitude(123.0)
                .longitude(321.0)
                .customer(customer)
                .build();
        Apartment apartment2 = Apartment.builder()
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("27")
                .apartmentNumber(5)
                .latitude(123.0)
                .longitude(321.0)
                .customer(customer)
                .build();
        Apartment apartment3 = Apartment.builder()
                .city("Terespol")
                .street("Piłsudskiego")
                .streetNumber("27")
                .apartmentNumber(5)
                .latitude(123.0)
                .longitude(321.0)
                .customer(customer)
                .build();
        List<Apartment> apartments = new ArrayList<>();
        apartments.add(apartment1);
        apartments.add(apartment2);
        apartments.add(apartment3);
        customer.setApartments(apartments);
        customerDbService.save(customer, passwordEncoder);
        apartmentDbService.save(apartment1);
        apartmentDbService.save(apartment2);
        apartmentDbService.save(apartment3);
        //When
        List<ApartmentDto> apartmentDtos = searcherDbFacade.searchApartments("Terespol", "Kraszewskiego");
        //Then
        assertEquals(2, apartmentDtos.size());
        apartmentDtos.stream()
                .allMatch(apartmentDto ->
                        apartmentDto.getCity().equals("Terespol") &&
                                apartmentDto.getStreet().equals("Kraszewkisego") &&
                                (apartmentDto.getStreetNumber().equals("26") || apartmentDto.getStreetNumber().equals("27")));
    }
}
