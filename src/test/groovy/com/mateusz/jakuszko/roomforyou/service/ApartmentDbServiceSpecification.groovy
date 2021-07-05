package com.mateusz.jakuszko.roomforyou.service

import com.mateusz.jakuszko.roomforyou.entity.Apartment
import com.mateusz.jakuszko.roomforyou.entity.Reservation
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException
import com.mateusz.jakuszko.roomforyou.repository.ApartmentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ApartmentDbServiceSpecification extends Specification {

    @Autowired
    private ApartmentDbService service
    @Autowired
    ApartmentRepository repository

    private static Apartment createApartment() {
        return Apartment.builder()
                .latitude(11.0)
                .longitude(12.0)
                .street("WallStreet")
                .streetNumber("13")
                .apartmentNumber(14)
                .reservations(new ArrayList<Reservation>())
                .build()
    }

    def cleanup() {
        repository.deleteAll()
    }

    def "should throw an exception when there isn't such apartment"() {
        when: "pull apartment from database which doesn't exist"
        Optional<Apartment> apartment = service.getApartment(123)
        apartment.orElseThrow(NotFoundException::new)

        then:
        thrown(NotFoundException)
    }

    def "should save and load apartment from db"() {
        given: "create apartment"
        Apartment apartment = createApartment()

        when: "save an apartment"
        service.save(apartment)
        Long apartmentId = apartment.getId()
        and: "pull apartment from database"
        Optional<Apartment> savedApartmentOptional = service.getApartment(apartmentId)
        Apartment savedApartment = savedApartmentOptional.orElseThrow(NotFoundException::new)

        then: "check if there is an exception"
        notThrown(NotFoundException)
        and: "assert all fields"
        savedApartment.getId() == apartmentId
        savedApartment.getReservations() == apartment.getReservations()
        savedApartment.getStreetNumber() == apartment.getStreetNumber()
        savedApartment.getApartmentNumber() == apartment.getApartmentNumber()
        savedApartment.getStreet() == apartment.getStreet()
        savedApartment.getLongitude() == apartment.getLongitude()
        savedApartment.getLatitude() == apartment.getLatitude()
        savedApartment.getCustomer() == apartment.getCustomer()
        savedApartment.getCity() == apartment.getCity()
    }

    def "should save apartments and return list of apartments after pull from db"() {
        given: "create apartments"
        Apartment apartment1 = createApartment()
        Apartment apartment2 = createApartment()
        and: "save apartments"
        service.save(apartment2)
        service.save(apartment1)

        when: "pull apartments from db"
        List<Apartment> apartments = service.getApartments()

        then: "size o apartments equals 2"
        apartments.size() == 2
        and: "assert all fields"
        apartments.get(0).getId() != null
        apartments.get(0).getLatitude() == 11
        apartments.get(0).getLongitude() == 12
        apartments.get(0).getStreet() == "WallStreet"
        apartments.get(0).getStreetNumber() == "13"
        apartments.get(0).getApartmentNumber() == 14
    }

    def "should update existing apartment"() {
        given: "create and save apartment into database"
        Apartment apartment = createApartment()
        service.save(apartment)
        Long apartmentId = apartment.getId()
        and: "pull apartment from database"
        Optional<Apartment> savedApartmentOptional = service.getApartment(apartmentId)
        Apartment savedApartment = savedApartmentOptional.orElseThrow(NotFoundException::new)

        when: "change fields and update apartment"
        savedApartment.setCity("Warsaw")
        savedApartment.setLatitude(23.2)
        savedApartment.setLongitude(52.2)
        savedApartment.setApartmentNumber(23)
        savedApartment.setStreet("Pilsudskiego")
        savedApartment.setStreetNumber("43")
        and: "update changed apartment"
        service.update(savedApartment)
        and: "pull updated apartment"
        Apartment updatedApartment = service.getApartment(apartmentId).orElseThrow(NotFoundException::new)

        then: "shouldn't throw an exception"
        notThrown(NotFoundException)
        and: "all changed fields should be correct"
        updatedApartment.getCity() == "Warsaw"
        updatedApartment.getLatitude() == 23.2
        updatedApartment.getLongitude() == 52.2
        updatedApartment.getApartmentNumber() == 23
        updatedApartment.getStreet() == "Pilsudskiego"
        updatedApartment.getStreetNumber() == "43"
    }

    def "should not found an apartment when it hase been deleted"() {
        given: "create and save apartment into database"
        Apartment apartment = createApartment()
        service.save(apartment)
        Long id = apartment.getId()

        when: "delete apartment and try to pull it from database"
        service.delete(id)
        service.getApartment(id).orElseThrow(NotFoundException::new)

        then: "apartment shouldn't found"
        thrown(NotFoundException)
    }
}
