package com.mateusz.jakuszko.roomforyou.controller;

import com.mateusz.jakuszko.roomforyou.dto.CustomerDto;
import com.mateusz.jakuszko.roomforyou.facade.CustomerDbFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/users")
public class CustomerController {

    private final CustomerDbFacade customerDbFacade;

    @GetMapping
    public List<CustomerDto> get() {
        log.info("Get all users");
        return customerDbFacade.getCustomers();
    }

    @GetMapping("/{id}")
    public CustomerDto get(@PathVariable Long id) {
        log.info("Get user by id - " + id);
        return customerDbFacade.getCustomer(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public CustomerDto create(@RequestBody CustomerDto customerDto) {
        log.info("Create user");
        return customerDbFacade.createCustomer(customerDto);
    }

    @PutMapping
    public CustomerDto update(@RequestBody CustomerDto customerDto) {
        return customerDbFacade.updateCustomer(customerDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        customerDbFacade.deleteCustomer(id);
    }
}
