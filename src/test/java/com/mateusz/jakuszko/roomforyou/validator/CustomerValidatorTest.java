package com.mateusz.jakuszko.roomforyou.validator;

import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.service.CustomerDbService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CustomerValidatorTest {

    @Autowired
    private CustomerValidator customerValidator;
    @Autowired
    private CustomerDbService customerDbService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void test() {
        //Given
        Customer customer = Customer.builder()
                .id(1L)
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .email("mateusz.jakuszko@gmail.com")
                .build();
        customerDbService.save(customer, passwordEncoder);
        //When
        boolean notUniqueCustomer = customerValidator.isUsernameUnique("matanos");
        boolean uniqueCustomer1 = customerValidator.isUsernameUnique("matanos1");
        boolean uniqueCustomer2 = customerValidator.isUsernameUnique("321");
        boolean uniqueCustomer3 = customerValidator.isUsernameUnique("");
        //Then
        assertFalse(notUniqueCustomer);
        assertTrue(uniqueCustomer1);
        assertTrue(uniqueCustomer2);
        assertTrue(uniqueCustomer3);
    }

}
