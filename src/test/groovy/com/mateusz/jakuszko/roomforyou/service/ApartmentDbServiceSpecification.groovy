package com.mateusz.jakuszko.roomforyou.service

import com.mateusz.jakuszko.roomforyou.TestcontainersSpecification
import com.mateusz.jakuszko.roomforyou.entity.Apartment
import com.mateusz.jakuszko.roomforyou.entity.Reservation
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException
import com.mateusz.jakuszko.roomforyou.repository.ApartmentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class ApartmentDbServiceSpecification extends TestcontainersSpecification {

    @Autowired
    private ApartmentDbService service
    @Autowired
    ApartmentRepository repository

    def "Should not fetch any apartment for nonexistent id"() {
        when:
        Optional<Apartment> apartment = service.getApartment(123)

        then:
        apartment.isEmpty()
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

    def "Should return list of apartments"() {
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
        service.save(apartment)
        Apartment savedApartment = service.getApartment(apartment.getId()).orElseThrow(NotFoundException::new)

        when:
        changeFieldsOfApartment(savedApartment)
        service.update(savedApartment)
        Optional <Apartment> updatedApartment = service.getApartment(savedApartment.getId())

        then:
        updatedApartment.isPresent()
        updatedApartment.get().getCity() == "Warsaw"
        updatedApartment.get().getLatitude() == 23.2
        updatedApartment.get().getLongitude() == 52.2
        updatedApartment.get().getApartmentNumber() == 23
        updatedApartment.get().getStreet() == "Pilsudskiego"
        updatedApartment.get().getStreetNumber() == "43"
    }

    def "should not found an apartment when it has been deleted"() {
        given: "create and save apartment into database"
        Apartment apartment = createApartment()
        service.save(apartment)
        Long id = apartment.getId()

        when: "delete apartment and try to pull it from database"
        service.delete(id)

        then: "apartment shouldn't found"
        service.getApartment(id).isEmpty()
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

    private static void changeFieldsOfApartment(Apartment savedApartment) {
        savedApartment.setCity("Warsaw")
        savedApartment.setLatitude(23.2)
        savedApartment.setLongitude(52.2)
        savedApartment.setApartmentNumber(23)
        savedApartment.setStreet("Pilsudskiego")
        savedApartment.setStreetNumber("43")
    }

    private Apartment saveAndGetApartmentFromDb(Apartment apartment) {
        service.save(apartment)
        Long apartmentId = apartment.getId()
        Optional<Apartment> savedApartmentOptional = service.getApartment(apartmentId)
        return savedApartmentOptional.orElseThrow(NotFoundException::new)
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
