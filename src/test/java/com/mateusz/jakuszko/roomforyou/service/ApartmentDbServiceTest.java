package com.mateusz.jakuszko.roomforyou.service;

import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApartmentDbServiceTest {

    @Autowired
    private ApartmentDbService apartmentDbService;

    private Apartment createApartment() {
        return Apartment.builder()
                .latitude(11.0)
                .longitude(12.0)
                .street("WallStreet")
                .streetNumber("13")
                .apartmentNumber(14)
                .build();
    }

    @Test
    public void whenSaveApartmentShouldBeAbleToGetThisApartmentFromDb() {
        //Given
        Apartment apartment = createApartment();
        //When
        apartmentDbService.save(apartment);
        Long apartmentId = apartment.getId();
        Optional<Apartment> savedApartment = apartmentDbService.getApartment(apartmentId);
        Apartment expectedApartment = createApartment();
        //Then
        assertTrue(savedApartment.isPresent());
        assertEquals(expectedApartment.getLatitude(), savedApartment.get().getLatitude());
        assertEquals(expectedApartment.getLongitude(), savedApartment.get().getLongitude());
        assertEquals(expectedApartment.getStreet(), savedApartment.get().getStreet());
        assertEquals(expectedApartment.getStreetNumber(), savedApartment.get().getStreetNumber());
        assertEquals(expectedApartment.getApartmentNumber(), savedApartment.get().getApartmentNumber());
    }

    @Test
    public void whenGetApartmentsFromDbShouldReturnListOfAllApartments() {
        //Given
        Apartment apartment1 = createApartment();
        Apartment apartment2 = createApartment();
        //When
        apartmentDbService.save(apartment1);
        apartmentDbService.save(apartment2);
        List<Apartment> apartments = apartmentDbService.getApartments();
        //Then
        assertEquals(2, apartments.size());
        assertTrue(apartments.stream()
                .allMatch(apartment -> apartment.getId() != null &&
                        apartment.getLatitude().equals(11.0) &&
                        apartment.getLongitude().equals(12.0) &&
                        apartment.getStreet().equals("WallStreet") &&
                        apartment.getStreetNumber().equals("13") &&
                        apartment.getApartmentNumber().equals(14)
                ));
    }

    @Test
    public void whenUpdateApartmentShouldReturnUpdatedApartmentFromDb() {
        //Given
        Apartment apartment = createApartment();
        //When
        apartmentDbService.save(apartment);
        Long apartmentId = apartment.getId();
        Apartment savedApartment = apartmentDbService.getApartment(apartmentId).orElseThrow(NotFoundException::new);
        savedApartment.setStreet("UpdatedStreet");
        apartmentDbService.update(savedApartment);
        Optional<Apartment> updatedApartment = apartmentDbService.getApartment(apartmentId);
        //Then
        assertTrue(updatedApartment.isPresent());
        assertEquals(11.0, updatedApartment.get().getLatitude(), 0);
        assertEquals(12.0, updatedApartment.get().getLongitude(), 0);
        assertEquals("UpdatedStreet", updatedApartment.get().getStreet());
        assertEquals("13", updatedApartment.get().getStreetNumber());
        assertEquals(14, updatedApartment.get().getApartmentNumber().intValue());
    }

    @Test
    public void whenDeleteApartmentShouldNotBeAbleToFindItInDb() {
        //Given
        Apartment apartment = createApartment();
        //When
        apartmentDbService.save(apartment);
        Long apartmentId = apartment.getId();
        apartmentDbService.delete(apartmentId);
        Optional<Apartment> savedApartment = apartmentDbService.getApartment(apartmentId);
        //Then
        assertFalse(savedApartment.isPresent());
    }
}
