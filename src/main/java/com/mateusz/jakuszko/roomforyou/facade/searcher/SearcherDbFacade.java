package com.mateusz.jakuszko.roomforyou.facade.searcher;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.entity.Reservation;
import com.mateusz.jakuszko.roomforyou.mapper.ApartmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearcherDbFacade {

    private final ApartmentMapper apartmentMapper;
    private final EntityManager entityManager;

    @Transactional
    public List<ApartmentDto> getApartments(String city, String street) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Apartment> criteriaQuery = criteriaBuilder
                .createQuery(Apartment.class);
        Root<Apartment> apartmentsRoot = criteriaQuery.from(Apartment.class);
        Predicate predicateForCity = criteriaBuilder.equal(apartmentsRoot.get("city"), city);
        Predicate predicateForStreet = criteriaBuilder.equal(apartmentsRoot.get("street"), street);
        if (city.length() > 0 && street.length() > 0) {
            Predicate finalPredicate = criteriaBuilder.and(predicateForCity, predicateForStreet);
            criteriaQuery.where(finalPredicate);
        } else if (city.length() > 0) {
            criteriaQuery.where(predicateForCity);
        } else if (street.length() > 0) {
            criteriaQuery.where(predicateForStreet);
        }
        return apartmentMapper.mapToApartmentDtos(entityManager.createQuery(criteriaQuery).getResultList());
    }

    @Transactional
    public List<Reservation> getReservationsIdThoseStartDateIsEarlierThanInAWeek() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reservation> criteriaQuery = criteriaBuilder.createQuery(Reservation.class);
        Root<Reservation> reservationRoot = criteriaQuery.from(Reservation.class);
        Predicate predicateStartDate = criteriaBuilder
                .between(reservationRoot.get("startDate"), LocalDate.now().minusDays(7), LocalDate.now());
        criteriaQuery.where(predicateStartDate);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
