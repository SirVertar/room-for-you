package com.mateusz.jakuszko.roomforyou.validator;

import com.mateusz.jakuszko.roomforyou.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Component
@RequiredArgsConstructor
public class CustomerValidator {
    private final EntityManager entityManager;

    public boolean isUsernameUnique(String username) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> apartmentsRoot = criteriaQuery.from(Customer.class);
        Predicate predicateForUsername = criteriaBuilder.equal(apartmentsRoot.get("username"), username);
        criteriaQuery.where(predicateForUsername);
        return entityManager.createQuery(criteriaQuery).getResultList().size() == 0;
    }
}
