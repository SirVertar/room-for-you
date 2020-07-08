package com.mateusz.jakuszko.roomforyou.repository;

import com.mateusz.jakuszko.roomforyou.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsername(String username);

}
