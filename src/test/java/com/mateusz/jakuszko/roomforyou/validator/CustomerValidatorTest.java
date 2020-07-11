package com.mateusz.jakuszko.roomforyou.validator;

import com.mateusz.jakuszko.roomforyou.entity.Customer;
import com.mateusz.jakuszko.roomforyou.service.CustomerDbService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Transactional
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
    public void whenValidateUsernameWhichISAlreadyInDbTheReturnsFlase() {
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
        //Then
        assertFalse(notUniqueCustomer);
    }

    @Test
    public void whenValidateUsernameWhichIsntInDbTheReturnsFlase() {
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
        boolean uniqueCustomer1 = customerValidator.isUsernameUnique("matanos1");
        boolean uniqueCustomer2 = customerValidator.isUsernameUnique("321");
        boolean uniqueCustomer3 = customerValidator.isUsernameUnique("");
        //Then
        assertTrue(uniqueCustomer1);
        assertTrue(uniqueCustomer2);
        assertTrue(uniqueCustomer3);
    }

    @Test
    public void whenValidatePasswordWithNotEnoughLongOrWithoutAnyNumberThenReturnFalse() {
        //When
        boolean password1NumberValidation = customerValidator.isPasswordConsistOfAtLeastOneNumber("1passwo");
        boolean password1LengthValidation = customerValidator.isPasswordEnoughLong("1passwo");
        boolean password2NumberValidation = customerValidator.isPasswordConsistOfAtLeastOneNumber("321");
        boolean password2LengthValidation = customerValidator.isPasswordEnoughLong("321");
        boolean password3NumberValidation = customerValidator.isPasswordConsistOfAtLeastOneNumber("password");
        boolean password3LengthValidation = customerValidator.isPasswordEnoughLong("password");
        //Then
        assertFalse(password1LengthValidation && password1NumberValidation);
        assertFalse(password2LengthValidation && password2NumberValidation);
        assertFalse(password3LengthValidation && password3NumberValidation);
    }
    @Test
    public void whenValidatePasswordWithEnoughLongAndWhichConsistsOfNumbersThenReturnTrue() {
        //When
        boolean password1NumberValidation = customerValidator.isPasswordConsistOfAtLeastOneNumber("123password");
        boolean password1LengthValidation = customerValidator.isPasswordEnoughLong("123password!");
        boolean password2NumberValidation = customerValidator.isPasswordConsistOfAtLeastOneNumber("qwer5tyd");
        boolean password2LengthValidation = customerValidator.isPasswordEnoughLong("qwer5tyd");
        boolean password3NumberValidation = customerValidator.isPasswordConsistOfAtLeastOneNumber("1!!??eqere");
        boolean password3LengthValidation = customerValidator.isPasswordEnoughLong("1!!??eqere");
        //Then
        assertTrue(password1LengthValidation && password1NumberValidation);
        assertTrue(password2LengthValidation && password2NumberValidation);
        assertTrue(password3LengthValidation && password3NumberValidation);
    }

}
