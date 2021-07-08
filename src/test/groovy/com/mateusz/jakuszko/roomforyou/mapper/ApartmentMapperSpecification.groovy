package com.mateusz.jakuszko.roomforyou.mapper

import com.mateusz.jakuszko.roomforyou.TestcontainersSpecification
import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto
import com.mateusz.jakuszko.roomforyou.entity.Apartment
import com.mateusz.jakuszko.roomforyou.entity.Customer
import com.mateusz.jakuszko.roomforyou.entity.Reservation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ApartmentMapperSpecification extends TestcontainersSpecification {

    @Autowired
    private ApartmentMapper apartmentMapper

    def "map Apartment to ApartmentDto class"() {
        given:
        Apartment apartment = createApartment()

        when:
        ApartmentDto apartmentDto = apartmentMapper.mapToApartmentDto(apartment)

        then:
        checkIfFieldsAreTheSame(apartment, apartmentDto)
    }

    def "map ApartmentDto to Apartment class"() {
        given:
        ApartmentDto apartmentDto = createApartmentDto()

        when:
        Apartment apartment = convertApartmentDtoToApartment(apartmentDto)

        then:
        checkIfFieldsAreTheSame(apartment, apartmentDto)
    }

    def "map apartments list to apartmentsDto list"() {
        given:
        List<Apartment> apartments = createApartmentsList()

        when:
        def apartmentDtos = apartmentMapper.mapToApartmentDtos(apartments)

        then:
        apartmentDtos.size() == 1
        checkIfFieldsAreTheSame(apartments.get(0), apartmentDtos.get(0))
    }

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

    private static List<Apartment> createApartmentsList() {
        Apartment apartment = createApartment()
        List<Apartment> apartments = [apartment]
        return apartments
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
                .build()
    }

    private Apartment convertApartmentDtoToApartment(ApartmentDto apartmentDto) {
        apartmentMapper.mapToApartment(
                apartmentDto,
                Collections.singletonList(Reservation.builder().id(11L).build()),
                Customer.builder().id(12L).build())
    }

    private static void checkIfFieldsAreTheSame(Apartment apartment, ApartmentDto apartmentDto) {
        assert apartment.getApartmentNumber() == apartmentDto.getApartmentNumber()
        assert apartment.getCity() == apartmentDto.getCity()
        assert apartment.getCustomer().getId() == apartmentDto.getCustomerId()
        assert apartment.getId() == apartmentDto.getId()
        assert apartment.getLatitude() == apartmentDto.getLatitude()
        assert apartment.getLongitude() == apartmentDto.getLongitude()
        assert apartment.getLongitude() == apartmentDto.getLongitude()
        assert apartment.getStreet() == apartmentDto.getStreet()
        assert apartment.getStreetNumber() == apartmentDto.getStreetNumber()
        assert apartment.getReservations().get(0).getId() == apartmentDto.getReservationsIds().get(0)
    }
}