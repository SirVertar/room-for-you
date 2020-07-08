package com.mateusz.jakuszko.roomforyou.facade.searcher;

import com.mateusz.jakuszko.roomforyou.dto.ApartmentDto;
import com.mateusz.jakuszko.roomforyou.entity.Apartment;
import com.mateusz.jakuszko.roomforyou.mapper.ApartmentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearcherDbFacade {

    private final ApartmentMapper apartmentMapper;
    private final EntityManager entityManager;

    public List<ApartmentDto> searchApartments(String city, String street) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Apartment> criteriaQuery = criteriaBuilder.createQuery(Apartment.class);
        Root<Apartment> apartmentsRoot = criteriaQuery.from(Apartment.class);
        Predicate predicateForCity = criteriaBuilder.equal(apartmentsRoot.get("city"), city);
        Predicate predicateForStreet = criteriaBuilder.equal(apartmentsRoot.get("street"), street);
        Predicate finalPredicate = criteriaBuilder.and(predicateForCity, predicateForStreet);
        criteriaQuery.where(finalPredicate);
        return apartmentMapper.mapToApartmentDtos(entityManager.createQuery(criteriaQuery).getResultList());
    }
}