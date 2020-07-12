package com.mateusz.jakuszko.roomforyou.facade.searcher;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService;
import com.mateusz.jakuszko.roomforyou.service.CustomerDbService;
import com.mateusz.jakuszko.roomforyou.service.ReservationDbService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
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
    private ReservationDbService reservationDbService;
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
        List<ApartmentDto> apartmentDtos = searcherDbFacade.getApartments("Terespol", "Kraszewskiego");
        //Then
        assertEquals(2, apartmentDtos.size());
        apartmentDtos.stream()
                .allMatch(apartmentDto ->
                        apartmentDto.getCity().equals("Terespol") &&
                                apartmentDto.getStreet().equals("Kraszewkisego") &&
                                (apartmentDto.getStreetNumber().equals("26") || apartmentDto.getStreetNumber().equals("27")));
    }

    @Test
    public void shouldReturnApartmentWithDateAfter1WeekBeforeToday() {
        //Given
        Reservation reservation1 = Reservation.builder()
                .startDate(LocalDate.now().minusDays(7))
                .endDate(LocalDate.now().plusDays(20))
                .build();
        Reservation reservation2 = Reservation.builder()
                .startDate(LocalDate.now().minusDays(8))
                .endDate(LocalDate.now().plusDays(20))
                .build();
        reservationDbService.save(reservation1);
        reservationDbService.save(reservation2);
        //When

        List<Reservation> reservations = searcherDbFacade.getReservationsIdThoseStartDateIsEarlierThanInAWeek();
        //Then
        assertEquals(1, reservations.size());
        assertEquals(LocalDate.now().minusDays(7), reservations.get(0).getStartDate());
        assertEquals(LocalDate.now().plusDays(20), reservations.get(0).getEndDate());
    }
}
