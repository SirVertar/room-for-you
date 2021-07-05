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

    def "should throw an exception when there isn't such apartment"() {
        when: "pull apartment from database which doesn't exist"
        Optional<Apartment> apartment = service.getApartment(123)
        apartment.orElseThrow(NotFoundException::new)

        then:
        thrown(NotFoundException)
    }

    def "should save and load apartment from db"() {
        given:
        Apartment apartment = createApartment()

        when:
        def savedApartment = saveAndGetApartmentFromDb(apartment)

        then:
        notThrown(NotFoundException)
        checkIfFieldsAreCorrect(savedApartment, apartment)
    }

    def "should save apartments and return list of apartments after pull from db"() {
        given:
        createAndSaveApartments()

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
        given:
        Apartment apartment = createApartment()
        Apartment savedApartment = saveAndGetApartmentFromDb(apartment)

        when:
        Apartment updatedApartment = changeFieldsUpdateAndGetApartmentFromDb(savedApartment)

        then:
        notThrown(NotFoundException)
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

    private Apartment changeFieldsUpdateAndGetApartmentFromDb(Apartment savedApartment) {
        savedApartment.setCity("Warsaw")
        savedApartment.setLatitude(23.2)
        savedApartment.setLongitude(52.2)
        savedApartment.setApartmentNumber(23)
        savedApartment.setStreet("Pilsudskiego")
        savedApartment.setStreetNumber("43")
        service.update(savedApartment)
        Apartment updatedApartment = service.getApartment(apartmentId).orElseThrow(NotFoundException::new)
        updatedApartment
    }

    private Apartment saveAndGetApartmentFromDb(Apartment apartment) {
        service.save(apartment)
        Long apartmentId = apartment.getId()
        Optional<Apartment> savedApartmentOptional = service.getApartment(apartmentId)
        savedApartmentOptional.orElseThrow(NotFoundException::new)
    }

    private static void checkIfFieldsAreCorrect(Apartment apartment1, Apartment apartment2) {
        assert apartment1.getId() == apartment2.getId()
        assert apartment1.getReservations() == apartment2.getReservations()
        assert apartment1.getStreetNumber() == apartment2.getStreetNumber()
        assert apartment1.getApartmentNumber() == apartment2.getApartmentNumber()
        assert apartment1.getStreet() == apartment2.getStreet()
        assert apartment1.getLongitude() == apartment2.getLongitude()
        assert apartment1.getLatitude() == apartment2.getLatitude()
        assert apartment1.getCustomer() == apartment2.getCustomer()
        assert apartment1.getCity() == apartment2.getCity()
    }

    private void createAndSaveApartments() {
        Apartment apartment1 = createApartment()
        Apartment apartment2 = createApartment()
        service.save(apartment2)
        service.save(apartment1)
    }

    def cleanup() {
        repository.deleteAll()
    }
}
