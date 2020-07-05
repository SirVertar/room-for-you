package com.mateusz.jakuszko.roomforyou.service;

import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDbService {

    private final UserRepository userRepository;

    public UserDbService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<Customer> getUser(Long id) {
        return userRepository.findById(id);
    }

    public List<Customer> getUsers() {
        return userRepository.findAll();
    }

    public Customer save(Customer customer, PasswordEncoder passwordEncoder) {
        String password = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(password);
        return userRepository.save(customer);
    }

    public Customer update(Customer customer) {
        return userRepository.save(customer);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<Customer> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
