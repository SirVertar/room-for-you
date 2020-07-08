package com.mateusz.jakuszko.roomforyou.facade.searcher;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.facade.ApartmentDbFacade;
import com.mateusz.jakuszko.roomforyou.facade.CustomerDbFacade;
import com.mateusz.jakuszko.roomforyou.mapper.ApartmentMapper;
import com.mateusz.jakuszko.roomforyou.mapper.CustomerMapper;
import com.mateusz.jakuszko.roomforyou.mapper.ReservationMapper;
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService;
import com.mateusz.jakuszko.roomforyou.service.CustomerDbService;
import com.mateusz.jakuszko.roomforyou.service.ReservationDbService;
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
    private ApartmentDbFacade apartmentDbFacade;
    @Autowired
    private CustomerDbFacade customerDbFacade;
    @Autowired
    private CustomerDbService customerDbService;
    @Autowired
    private ApartmentDbService apartmentDbService;
    @Autowired
    private ReservationDbService reservationDbService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ApartmentMapper apartmentMapper;
    @Autowired
    private ReservationMapper reservationMapper;
    @Autowired
    private SearcherDbFacade searcherDbFacade;

    @Test
    public void searchApartmentsTest() {
        Customer customer = Customer.builder()
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .role("Admin")
                .email("mateusz.jakuszko@gmail.com")
                .build();
        com.mateusz.jakuszko.roomforyou.entity.Apartment apartment1 = com.mateusz.jakuszko.roomforyou.entity.Apartment.builder()
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("26")
                .apartmentNumber(5)
                .latitude(123.0)
                .longitude(321.0)
                .customer(customer)
                .build();
        com.mateusz.jakuszko.roomforyou.entity.Apartment apartment2 = com.mateusz.jakuszko.roomforyou.entity.Apartment.builder()
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("27")
                .apartmentNumber(5)
                .latitude(123.0)
                .longitude(321.0)
                .customer(customer)
                .build();
        com.mateusz.jakuszko.roomforyou.entity.Apartment apartment3 = com.mateusz.jakuszko.roomforyou.entity.Apartment.builder()
                .city("Terespol")
                .street("Piłsudskiego")
                .streetNumber("27")
                .apartmentNumber(5)
                .latitude(123.0)
                .longitude(321.0)
                .customer(customer)
                .build();


        List<com.mateusz.jakuszko.roomforyou.entity.Apartment> apartments = new ArrayList<>();
        apartments.add(apartment1);
        apartments.add(apartment2);
        apartments.add(apartment3);

        customer.setApartments(apartments);

        customerDbService.save(customer, passwordEncoder);
        apartmentDbService.save(apartment1);
        apartmentDbService.save(apartment2);
        apartmentDbService.save(apartment3);


        List<ApartmentDto> ApartmentDtos = searcherDbFacade.searchApartments("Terespol", "Kraszewskiego");

        assertEquals(2, ApartmentDtos.size());
        assertEquals(2, ApartmentDtos.size());
        assertEquals(2, ApartmentDtos.size());
    }
}
