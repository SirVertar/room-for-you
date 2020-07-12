package com.mateusz.jakuszko.roomforyou;

import com.mateusz.jakuszko.roomforyou.dto.Mail;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.scheduler.EmailScheduler;
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService;
import com.mateusz.jakuszko.roomforyou.service.CustomerDbService;
import com.mateusz.jakuszko.roomforyou.service.EmailService;
import com.mateusz.jakuszko.roomforyou.service.ReservationDbService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class test {

    @Autowired
    private EmailService emailService;
    @Autowired
    private CustomerDbService customerDbService;
    @Autowired
    private ApartmentDbService apartmentDbService;
    @Autowired
    private ReservationDbService reservationDbService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailScheduler emailScheduler;


    private List<Long> prepareAndSaveDataIntoDbAndReturnDataIds() {
        Customer customer = Customer.builder()
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .role("Admin")
                .email("mateusz.jakuszko@gmail.com")
                .build();

        Apartment apartment = Apartment.builder()
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("26")
                .apartmentNumber(5)
                .latitude(123.0)
                .longitude(321.0)
                .customer(customer)
                .build();
        Reservation reservation = Reservation.builder()
                .startDate(LocalDate.now().minusDays(4))
                .endDate(LocalDate.now().plusDays(11))
                .apartment(apartment)
                .customer(customer)
                .build();

        List<Apartment> apartments = new ArrayList<>();
        apartments.add(apartment);
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        apartment.setReservations(reservations);

        customer.setApartments(apartments);
        customer.setReservations(reservations);

        apartmentDbService.save(apartment);
        reservationDbService.save(reservation);
        customerDbService.save(customer, passwordEncoder);

        List<Long> ids = new ArrayList<>();
        ids.add(customer.getId());
        ids.add(apartment.getId());
        ids.add(reservation.getId());
        return ids;
    }

    @Test
    public void tryingSendMail() throws MessagingException {
        Mail mail = Mail.builder().mailTo("mateusz.jakuszko.kodilla@gmail.com").subject("Powrót do domu")
                .message("Dzień dobry, pozdrawiam").build();
        emailService.sendMail(mail, false);
    }

    @Test
    public void aaa() {
        prepareAndSaveDataIntoDbAndReturnDataIds();
        emailScheduler.sendOnceADayMailForAllCustomersWhoHasReservationsEarlierThanInAWeek();
    }


}
