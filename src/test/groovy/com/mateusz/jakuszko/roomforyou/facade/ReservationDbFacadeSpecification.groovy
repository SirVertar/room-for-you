package com.mateusz.jakuszko.roomforyou.facade

import com.mateusz.jakuszko.roomforyou.dto.ReservationDto
import com.mateusz.jakuszko.roomforyou.entity.Apartment
import com.mateusz.jakuszko.roomforyou.entity.Customer
import com.mateusz.jakuszko.roomforyou.exceptions.InvalidReservationDateException
import com.mateusz.jakuszko.roomforyou.repository.ReservationRepository
import com.mateusz.jakuszko.roomforyou.service.ApartmentDbService
import com.mateusz.jakuszko.roomforyou.service.CustomerDbService
import com.mateusz.jakuszko.roomforyou.service.ReservationDbService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

@SpringBootTest
class ReservationDbFacadeSpecification extends Specification {

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
    @Autowired
    private ReservationRepository repository

    private Customer customer
    private Apartment apartment

    def setup() {
        createCustomer()
        createApartment(customer)
    }

    def cleanup() {
        repository.deleteAll()
    }

    def "should fetch reservation from db"() {
        given:
        ReservationDto reservationDto = createReservationDto(LocalDate.now().plusDays(20),
                LocalDate.now().plusDays(24),
                customer,
                apartment)
        Long id = reservationDbFacade.createReservation(reservationDto).getId()

        when:
        ReservationDto reservation = reservationDbFacade.getReservation(id)

        then:
        verifyAllFields(reservation, LocalDate.now().plusDays(20),
                LocalDate.now().plusDays(24), customer.getId(), apartment.getId())
    }

    def "should create reservation"() {
        given:
        ReservationDto reservationDto = createReservationDto(LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(8),
                customer,
                apartment)

        when:
        ReservationDto createdReservation = reservationDbFacade.createReservation(reservationDto)

        then:
        reservationDbService.getReservation(createdReservation.getId()).isPresent()
        verifyAllFields(createdReservation, LocalDate.now().plusDays(5),
                LocalDate.now().plusDays(8), customer.getId(), apartment.getId())
    }

    @Unroll('start - #startDate, end - #endDate')
    def "shouldn't create a reservation because of invalid reservation dates"() {
        given:
        ReservationDto reservationDto = createReservationDto(LocalDate.now().plusDays(20),
                LocalDate.now().plusDays(24),
                customer,
                apartment)
        reservationDbFacade.createReservation(reservationDto)

        when:
        ReservationDto reservation = reservationDbFacade.createReservation(createReservationDto(startDate, endDate,
                customer, apartment))

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

    @Unroll('start - #startDate, end - #endDate')
    def "should create a reservation - correct reservation dates"() {
        given:
        ReservationDto reservationDto = createReservationDto(startDate, endDate, customer, apartment)

        when:
        Long id = reservationDbFacade.createReservation(reservationDto).getId()

        then:
        verifyAllFields(reservationDbFacade.getReservation(id), startDate, endDate, customer.getId(), apartment.getId())

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
        ReservationDto reservationDto = createReservationDto(LocalDate.now().plusDays(20),
                LocalDate.now().plusDays(24),
                customer,
                apartment)
        Long id = reservationDbFacade.createReservation(reservationDto).getId()
        ReservationDto fetchedReservation = reservationDbFacade.getReservation(id)
        updateReservation(fetchedReservation, LocalDate.now().plusDays(22), LocalDate.now().plusDays(26))

        when:
        reservationDbFacade.updateReservation(fetchedReservation)

        then:
        verifyAllFields(reservationDbFacade.getReservation(id),
                LocalDate.now().plusDays(22),
                LocalDate.now().plusDays(26),
                customer.getId(),
                apartment.getId())
    }

    def "should delete reservation"() {
        given:
        ReservationDto reservationDto = createReservationDto(LocalDate.now().plusDays(20),
                LocalDate.now().plusDays(24),
                customer,
                apartment)
        Long id = reservationDbFacade.createReservation(reservationDto).getId()

        when:
        reservationDbFacade.deleteReservation(id)

        then:
        reservationDbService.getReservation(id).isEmpty()
    }

    private void createCustomer() {
        Customer customer = Customer.builder()
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .role("Admin")
                .email("mateusz.jakuszko@gmail.com")
                .build()
        this.customer = customerDbService.save(customer, passwordEncoder)
    }

    private void createApartment(Customer customer) {
        Apartment apartment = Apartment.builder()
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("26")
                .apartmentNumber(5)
                .latitude(123.0)
                .longitude(321.0)
                .customer(customer)
                .build()
        this.apartment = apartmentDbService.save(apartment)
    }

    private static void verifyAllFields(ReservationDto reservation, LocalDate startDate, LocalDate endDate, Long customerId, Long ApartmentId) {
        assert reservation.getCustomerId() == customerId
        assert reservation.getApartmentId() == ApartmentId
        assert reservation.getStartDate() == startDate
        assert reservation.getEndDate() == endDate
    }

    private static ReservationDto createReservationDto(LocalDate startDate, LocalDate endDate, Customer customer, Apartment apartment) {
        return ReservationDto.builder()
                .apartmentId(apartment.getId())
                .customerId(customer.getId())
                .startDate(startDate)
                .endDate(endDate).build()
    }

    private static void updateReservation(reservationDto, LocalDate startDate, LocalDate endDate) {
        reservationDto.setStartDate(startDate)
        reservationDto.setEndDate(endDate)
    }
}
