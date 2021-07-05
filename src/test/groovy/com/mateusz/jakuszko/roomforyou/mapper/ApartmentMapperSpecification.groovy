package com.mateusz.jakuszko.roomforyou.mapper

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto
import com.mateusz.jakuszko.roomforyou.entity.Apartment
import com.mateusz.jakuszko.roomforyou.entity.Customer
import com.mateusz.jakuszko.roomforyou.entity.Reservation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ApartmentMapperSpecification extends Specification {

    @Autowired
    private ApartmentMapper apartmentMapper

    private static Apartment createApartment() {
        return Apartment.builder()
                .id(1L)
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("26")
                .apartmentNumber(5)
                .latitude(123.0)
                .longitude(321.0)
                .reservations(Collections.singletonList(Reservation.builder().id(11L).build()))
                .customer(Customer.builder().id(12L).build())
                .build()
    }

    private static ApartmentDto createApartmentDto() {
        return new ApartmentDto.Builder()
                .id(1L)
                .city("Terespol")
                .street("Kraszewskiego")
                .streetNumber("26")
                .apartmentNumber(5)
                .latitude(123.0)
                .longitude(321.0)
                .customerId(12L)
                .reservationsIds(Collections.singletonList(11L))
                .build();
    }

    def "assert bean creation"() {
        expect:
        apartmentMapper != null
    }

    def "aap Apartment to ApartmentDto class"() {

        given: "create apartment"
        Apartment apartment = createApartment()

        when: "map apartment to apartmentDto"
        ApartmentDto apartmentDto = apartmentMapper.mapToApartmentDto(apartment)

        then: "assert all fields"
        apartment.getApartmentNumber() == apartmentDto.getApartmentNumber()
        apartment.getCity() == apartmentDto.getCity()
        apartment.getCustomer().getId() == apartmentDto.getCustomerId()
        apartment.getId() == apartmentDto.getId()
        apartment.getLatitude() == apartmentDto.getLatitude()
        apartment.getLongitude() == apartmentDto.getLongitude()
        apartment.getLongitude() == apartmentDto.getLongitude()
        apartment.getStreet() == apartmentDto.getStreet()
        apartment.getStreetNumber() == apartmentDto.getStreetNumber()
        apartment.getReservations().get(0).getId() == apartmentDto.getReservationsIds().get(0)
    }

    def "map ApartmentDto to Apartment class"() {

        given: "create apartment"
        ApartmentDto apartmentDto = createApartmentDto()


        when: "map apartment to apartmentDto"
        Apartment apartment = apartmentMapper.mapToApartment(
                apartmentDto,
                Collections.singletonList(Reservation.builder().id(11L).build()),
                Customer.builder().id(12L).build())

        then: "assert all fields"
        apartment.getApartmentNumber() == apartmentDto.getApartmentNumber()
        apartment.getCity() == apartmentDto.getCity()
        apartment.getCustomer().getId() == apartmentDto.getCustomerId()
        apartment.getId() == apartmentDto.getId()
        apartment.getLatitude() == apartmentDto.getLatitude()
        apartment.getLongitude() == apartmentDto.getLongitude()
        apartment.getLongitude() == apartmentDto.getLongitude()
        apartment.getStreet() == apartmentDto.getStreet()
        apartment.getStreetNumber() == apartmentDto.getStreetNumber()
        apartment.getReservations().get(0).getId() == apartmentDto.getReservationsIds().get(0)
    }

    def "map apartments list to apartmentsDto list"() {
        given: "create apartments list"
        Apartment apartment = createApartment()
        List<Apartment> apartments = new ArrayList<>()
        apartments << apartment

        when: "map apartment list to apartmentDto list"
        def apartmentDtos = apartmentMapper.mapToApartmentDtos(apartments)

        then: "assert all fields"
        apartmentDtos.size() == 1
        apartment.getApartmentNumber() == apartmentDtos.get(0).getApartmentNumber()
        apartment.getCity() == apartmentDtos.get(0).getCity()
        apartment.getCustomer().getId() == apartmentDtos.get(0).getCustomerId()
        apartment.getId() == apartmentDtos.get(0).getId()
        apartment.getLatitude() == apartmentDtos.get(0).getLatitude()
        apartment.getLongitude() == apartmentDtos.get(0).getLongitude()
        apartment.getLongitude() == apartmentDtos.get(0).getLongitude()
        apartment.getStreet() == apartmentDtos.get(0).getStreet()
        apartment.getStreetNumber() == apartmentDtos.get(0).getStreetNumber()
        apartment.getReservations().get(0).getId() == apartmentDtos.get(0).getReservationsIds().get(0)
    }
}