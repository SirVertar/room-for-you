package com.mateusz.jakuszko.roomforyou.facade

import com.mateusz.jakuszko.roomforyou.dto.ReservationDto
import com.mateusz.jakuszko.roomforyou.entity.Apartment
import com.mateusz.jakuszko.roomforyou.entity.Customer
import com.mateusz.jakuszko.roomforyou.entity.Reservation
import com.mateusz.jakuszko.roomforyou.exceptions.InvalidReservationDateException
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService
import com.mateusz.jakuszko.roomforyou.service.CustomerDbService
import com.mateusz.jakuszko.roomforyou.service.ReservationDbService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

import java.time.LocalDate

@SpringBootTest
class ReservationDbFacadeSpecification extends Specification {

    private static final int CUSTOMER_INDEX = 0
    private static final int APARTMENT_INDEX = 1
    private static final int RESERVATION_INDEX = 2
    private List<Long> idsList

    @Autowired
    private CustomerDbService customerDbService
    @Autowired
    private ApartmentDbService apartmentDbService
    @Autowired
    private ReservationDbService reservationDbService
    @Autowired
    private PasswordEncoder passwordEncoder
    @Autowired
    private ReservationDbFacade reservationDbFacade

    def setup() {
        idsList = prepareData()
    }

    def "should fetch reservation from db"() {
        when:
        ReservationDto reservation = reservationDbFacade.getReservation(idsList.get(RESERVATION_INDEX))
        then:
        verifyAllFields(reservation, LocalDate.now().plusDays(20), LocalDate.now().plusDays(24))
    }

    def "should create reservation"() {
        given:
        ReservationDto reservationDto = createReservation(LocalDate.now().plusDays(5), LocalDate.now().plusDays(8))
        when:
        ReservationDto createdReservation = reservationDbFacade.createReservation(reservationDto)
        then:
        reservationDbService.getReservation(createdReservation.getId()).isPresent()
        verifyAllFields(createdReservation, LocalDate.now().plusDays(5), LocalDate.now().plusDays(8))
    }

    def "shouldn't create a reservation - invalid reservation dates"() {
        when:
        ReservationDto reservation = reservationDbFacade.createReservation(createReservation(startDate, endDate))

        then:
        reservation == null
        thrown(InvalidReservationDateException)

        where:
        startDate                    | endDate
        LocalDate.now().plusDays(1)  | LocalDate.now().plusDays(0)
        LocalDate.now().plusDays(0)  | LocalDate.now().minusDays(1)
        LocalDate.now().plusDays(5)  | LocalDate.now().plusDays(1)
        LocalDate.now().plusDays(20) | LocalDate.now().plusDays(22)
        LocalDate.now().plusDays(23) | LocalDate.now().plusDays(25)
        LocalDate.now().plusDays(22) | LocalDate.now().plusDays(23)
        LocalDate.now().minusDays(3) | LocalDate.now().plusDays(9)
        null                         | LocalDate.now().plusDays(9)
        null                         | null
        LocalDate.now().plusDays(0)  | null
    }

    def "should create a reservation - correct reservation dates"() {
        expect:
        reservationDbFacade.createReservation(createReservation(startDate, endDate)) != null

        where:
        startDate                    | endDate
        LocalDate.now().plusDays(0)  | LocalDate.now().plusDays(0)
        LocalDate.now().plusDays(0)  | LocalDate.now().plusDays(1)
        LocalDate.now().plusDays(1)  | LocalDate.now().plusDays(3)
        LocalDate.now().plusDays(24) | LocalDate.now().plusDays(26)
        LocalDate.now().plusDays(18) | LocalDate.now().plusDays(20)
    }

    def "should update a reservation"() {
        given:
        ReservationDto reservationDto = reservationDbFacade.getReservation(idsList.get(RESERVATION_INDEX))
        when:
        updateReservation(reservationDto)
        reservationDbFacade.updateReservation(reservationDto)
        then:
        verifyAllFields(reservationDto,
                LocalDate.now().plusDays(22),
                LocalDate.now().plusDays(26))
    }

    def "should delete reservation"() {
        when:
        reservationDbFacade.deleteReservation(idsList.get(RESERVATION_INDEX))
        then:
        reservationDbService.getReservation(idsList.get(RESERVATION_INDEX)).isEmpty()
    }


    private List<Long> prepareData() {
        Customer customer = Customer.builder()
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .role("Admin")
                .email("mateusz.jakuszko@gmail.com")
                .build()

        Apartment apartment = Apartment.builder()
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("26")
                .apartmentNumber(5)
                .latitude(123.0)
                .longitude(321.0)
                .customer(customer)
                .build()

        Reservation reservation = Reservation.builder()
                .startDate(LocalDate.now().plusDays(20))
                .endDate(LocalDate.now().plusDays(24))
                .apartment(apartment)
                .customer(customer)
                .build()

        customerDbService.save(customer, passwordEncoder)
        apartmentDbService.save(apartment)

        List<Apartment> apartments = [apartment]
        List<Reservation> reservations = [reservation]
        apartment.setReservations(reservations)

        customer.setApartments(apartments)
        customer.setReservations(reservations)

        reservationDbService.save(reservation)
        apartmentDbService.update(apartment)
        customerDbService.update(customer)

        List<Long> ids = new ArrayList<>()
        ids.add(CUSTOMER_INDEX, customer.getId())
        ids.add(APARTMENT_INDEX, apartment.getId())
        ids.add(RESERVATION_INDEX, reservation.getId())
        return ids
    }

    private void verifyAllFields(ReservationDto reservation, LocalDate startDate, LocalDate endDate) {
        assert reservation.getCustomerId() == idsList.get(CUSTOMER_INDEX)
        assert reservation.getApartmentId() == idsList.get(APARTMENT_INDEX)
        assert reservation.getStartDate() == startDate
        assert reservation.getEndDate() == endDate
    }

    private ReservationDto createReservation(LocalDate startDate, LocalDate endDate) {
        return ReservationDto.builder()
                .apartmentId(idsList.get(APARTMENT_INDEX))
                .customerId(idsList.get(CUSTOMER_INDEX))
                .startDate(startDate)
                .endDate(endDate).build()
    }

    private static void updateReservation(reservationDto) {
        reservationDto.setStartDate(reservationDto.getStartDate().plusDays(2))
        reservationDto.setEndDate(reservationDto.getEndDate().plusDays(2))
    }

}
