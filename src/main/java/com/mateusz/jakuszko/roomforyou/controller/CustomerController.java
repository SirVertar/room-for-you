package com.mateusz.jakuszko.roomforyou.controller;

import com.mateusz.jakuszko.roomforyou.dto.CustomerDto;
import com.mateusz.jakuszko.roomforyou.exceptions.NotUniqueUsernameException;
import com.mateusz.jakuszko.roomforyou.exceptions.PasswordIncorrectException;
import com.mateusz.jakuszko.roomforyou.facade.CustomerDbFacade;
import com.mateusz.jakuszko.roomforyou.validator.CustomerValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/customers")
public class CustomerController {
    private final CustomerDbFacade customerDbFacade;
    private final CustomerValidator customerValidator;

    @GetMapping
    public List<CustomerDto> get() {
        log.info("Get all customers");
        return customerDbFacade.getCustomers();
    }

    @GetMapping("/{id}")
    public CustomerDto get(@PathVariable Long id) {
        log.info("Get customer by id - " + id);
        return customerDbFacade.getCustomer(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public CustomerDto create(@RequestBody CustomerDto customerDto) {
        if (!customerValidator.isUsernameUnique(customerDto.getUsername())) {
            log.warn("Username is not unique");
            throw new NotUniqueUsernameException();
        }
        if (!customerValidator.isPasswordEnoughLong(customerDto.getPassword())) {
            log.warn("Password is to short, should be minimum: " + CustomerValidator.MIN_PASSWORD_LENGTH + " length");
            throw new PasswordIncorrectException();
        }
        if (!customerValidator.isPasswordConsistOfAtLeastOneNumber(customerDto.getPassword())) {
            log.warn("Password has any numbers");
            throw new PasswordIncorrectException();
        }
        log.info("Create customer__username - " + customerDto.getUsername());
        return customerDbFacade.createCustomer(customerDto);
    }

    @PutMapping
    public CustomerDto update(@RequestBody CustomerDto customerDto) {
        log.info("Update customer");
        return customerDbFacade.updateCustomer(customerDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("delete customer by id - " + id);
        customerDbFacade.deleteCustomer(id);
    }
}
