package com.mateusz.jakuszko.roomforyou.service;

import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerDbServiceTest {

    @Autowired
    private CustomerDbService customerDbService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Customer createUser() {
        return Customer.builder()
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .role("Admin")
                .email("mateusz.jakuszko@gmail.com")
                .build();
    }

    @Test
    public void whenSaveCustomerShouldBeAbleToGetThisCustomerFromDb() {
        //Given
        Customer customer = createUser();
        //When
        customerDbService.save(customer, passwordEncoder);
        Long userId = customer.getId();
        Optional<Customer> savedUser = customerDbService.getUser(userId);
        //Then
        assertNotNull(customer.getId());
        assertTrue(savedUser.isPresent());
        assertEquals("Mateusz", savedUser.get().getName());
        assertEquals("Jakuszko", savedUser.get().getSurname());
        assertEquals("matanos", savedUser.get().getUsername());
        assertEquals("Admin", savedUser.get().getRole());
        assertEquals("mateusz.jakuszko@gmail.com", savedUser.get().getEmail());
        assertTrue(passwordEncoder.matches("abc123", savedUser.get().getPassword()));
    }

    @Test
    public void whenGetCustomersFromDbShouldReturnListOfAllCustomers() {
        //Given
        Customer customer1 = createUser();
        Customer customer2 = createUser();
        customerDbService.save(customer1, passwordEncoder);
        customerDbService.save(customer2, passwordEncoder);
        //When
        List<Customer> customers = customerDbService.getUsers();
        //Then
        assertEquals(2, customers.size());
        assertTrue(customers.stream()
                .allMatch(user -> user.getId() != null &&
                        user.getEmail().equals("mateusz.jakuszko@gmail.com") &&
                        user.getName().equals("Mateusz") &&
                        user.getSurname().equals("Jakuszko") &&
                        user.getUsername().equals("matanos") &&
                        user.getRole().equals("Admin") &&
                        passwordEncoder.matches("abc123", user.getPassword())));
    }

    @Test
    public void whenUpdateCustomerShouldReturnUpdatedCustomerFromDb() {
        //Given
        Customer customer = createUser();
        //When
        customerDbService.save(customer, passwordEncoder);
        Long userId = customer.getId();
        Customer customerAfterSave = customerDbService.getUser(userId).orElseThrow(NotFoundException::new);
        customerAfterSave.setEmail("changedMail");
        customerDbService.update(customerAfterSave);
        Optional<Customer> userAfterUpdate = customerDbService.getUser(userId);
        //Then
        assertTrue(userAfterUpdate.isPresent());
        assertEquals("Mateusz", userAfterUpdate.get().getName());
        assertEquals("Jakuszko", userAfterUpdate.get().getSurname());
        assertEquals("matanos", userAfterUpdate.get().getUsername());
        assertEquals("Admin", userAfterUpdate.get().getRole());
        assertEquals("changedMail", userAfterUpdate.get().getEmail());
        assertTrue(passwordEncoder.matches("abc123", userAfterUpdate.get().getPassword()));
    }

    @Test
    public void whenDeleteCustomerShouldNotBeAbleToFindItInDb() {
        //Given
        Customer customer = createUser();
        //When
        customerDbService.save(customer, passwordEncoder);
        Long userId = customer.getId();
        customerDbService.delete(userId);
        Optional<Customer> deletedUser = customerDbService.getUser(userId);
        //Then
        assertNotNull(customer.getId());
        assertFalse(deletedUser.isPresent());
    }
}
