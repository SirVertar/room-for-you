package com.mateusz.jakuszko.roomforyou.service.deleted;

import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedCustomer;
import com.mateusz.jakuszko.roomforyou.repository.deleted.DeletedCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeletedCustomerDbService {
    private final DeletedCustomerRepository customerRepository;

    public Optional<DeletedCustomer> getUser(Long id) {
        return customerRepository.findById(id);
    }

    public List<DeletedCustomer> getUsers() {
        return customerRepository.findAll();
    }

    public DeletedCustomer save(DeletedCustomer customer, PasswordEncoder passwordEncoder) {
        String password = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(password);
        return customerRepository.save(customer);
    }

    public DeletedCustomer update(DeletedCustomer customer) {
        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        customerRepository.deleteById(id);
    }
}
