package com.mateusz.jakuszko.roomforyou.service;

import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.repository.CustomerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerDbService {

    private final CustomerRepository customerRepository;

    public CustomerDbService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Optional<Customer> getUser(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> getUsers() {
        return customerRepository.findAll();
    }

    public Customer save(Customer customer, PasswordEncoder passwordEncoder) {
        String password = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(password);
        return customerRepository.save(customer);
    }

    public Customer update(Customer customer) {
        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }

    public Optional<Customer> getByUsername(String username) {
        return customerRepository.findByUsername(username);
    }
}
