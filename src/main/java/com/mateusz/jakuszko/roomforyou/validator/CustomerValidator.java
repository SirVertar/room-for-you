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
    public static final int MIN_PASSWORD_LENGTH = 8;

    public boolean isUsernameUnique(String username) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> criteriaQuery = criteriaBuilder.createQuery(Customer.class);
        Root<Customer> apartmentsRoot = criteriaQuery.from(Customer.class);
        Predicate predicateForUsername = criteriaBuilder.equal(apartmentsRoot.get("username"), username);
        criteriaQuery.where(predicateForUsername);
        return entityManager.createQuery(criteriaQuery).getResultList().size() == 0;
    }

    public boolean isPasswordEnoughLong(String password) {
        return password.length() >= MIN_PASSWORD_LENGTH;
    }

    public boolean isPasswordConsistOfAtLeastOneNumber(String password) {
        char[] passwordArray = password.toCharArray();
        for (char character : passwordArray) {
            if (checkIsCharacterNumber(character)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIsCharacterNumber(char character) {
        switch (character) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return true;
        }
        return false;
    }
}
