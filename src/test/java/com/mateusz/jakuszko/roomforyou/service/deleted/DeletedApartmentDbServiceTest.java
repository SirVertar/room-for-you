package com.mateusz.jakuszko.roomforyou.service.deleted;

import com.mateusz.jakuszko.roomforyou.clearer.TableClearer;
import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedApartment;
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
public class DeletedApartmentDbServiceTest {
    @Autowired
    private DeletedApartmentDbService apartmentDbService;
    @Autowired
    private TableClearer clearer;

    private DeletedApartment createApartment() {
        return DeletedApartment.builder()
                .latitude(11.0)
                .longitude(12.0)
                .street("WallStreet")
                .streetNumber("13")
                .apartmentNumber(14)
                .previousApartmentId(111L)
                .build();
    }

    @Test
    public void whenSaveDeletedApartmentShouldBeAbleToGetThisApartmentFromDb() {
        //Given
        DeletedApartment apartment = createApartment();
        //When
        apartmentDbService.save(apartment);
        Long apartmentId = apartment.getId();
        Optional<DeletedApartment> savedApartment = apartmentDbService.getApartment(apartmentId);
        DeletedApartment expectedApartment = createApartment();
        //Then
        assertTrue(savedApartment.isPresent());
        assertEquals(expectedApartment.getLatitude(), savedApartment.get().getLatitude());
        assertEquals(expectedApartment.getLongitude(), savedApartment.get().getLongitude());
        assertEquals(expectedApartment.getStreet(), savedApartment.get().getStreet());
        assertEquals(expectedApartment.getStreetNumber(), savedApartment.get().getStreetNumber());
        assertEquals(expectedApartment.getApartmentNumber(), savedApartment.get().getApartmentNumber());
        assertEquals(expectedApartment.getPreviousApartmentId(), savedApartment.get().getPreviousApartmentId());
    }

    @Test
    public void whenGetDeletedApartmentsFromDbShouldReturnListOfAllApartments() {
        //Given
        DeletedApartment apartment1 = createApartment();
        DeletedApartment apartment2 = createApartment();
        //When
        apartmentDbService.save(apartment1);
        apartmentDbService.save(apartment2);
        List<DeletedApartment> apartments = apartmentDbService.getApartments();
        //Then
        assertEquals(2, apartments.size());
        assertTrue(apartments.stream()
                .allMatch(apartment -> apartment.getId() != null &&
                        apartment.getLatitude().equals(11.0) &&
                        apartment.getLongitude().equals(12.0) &&
                        apartment.getStreet().equals("WallStreet") &&
                        apartment.getStreetNumber().equals("13") &&
                        apartment.getApartmentNumber().equals(14) &&
                        apartment.getPreviousApartmentId().equals(111L)
                ));
    }

    @Test
    public void whenUpdateDeletedApartmentShouldReturnUpdatedApartmentFromDb() {
        //Given
        DeletedApartment apartment = createApartment();
        //When
        apartmentDbService.save(apartment);
        Long apartmentId = apartment.getId();
        DeletedApartment savedApartment = apartmentDbService.getApartment(apartmentId).orElseThrow(NotFoundException::new);
        savedApartment.setStreet("UpdatedStreet");
        apartmentDbService.update(savedApartment);
        Optional<DeletedApartment> updatedApartment = apartmentDbService.getApartment(apartmentId);
        //Then
        assertTrue(updatedApartment.isPresent());
        assertEquals(11.0, updatedApartment.get().getLatitude(), 0);
        assertEquals(12.0, updatedApartment.get().getLongitude(), 0);
        assertEquals("UpdatedStreet", updatedApartment.get().getStreet());
        assertEquals("13", updatedApartment.get().getStreetNumber());
        assertEquals(14, updatedApartment.get().getApartmentNumber().intValue());
        assertEquals(111L, updatedApartment.get().getPreviousApartmentId().intValue());
    }

    @Test
    public void whenDeleteDeletedApartmentShouldNotBeAbleToFindItInDb() {
        //Given
        DeletedApartment apartment = createApartment();
        //When
        apartmentDbService.save(apartment);
        Long apartmentId = apartment.getId();
        apartmentDbService.delete(apartmentId);
        Optional<DeletedApartment> savedApartment = apartmentDbService.getApartment(apartmentId);
        //Then
        assertFalse(savedApartment.isPresent());
    }
}
