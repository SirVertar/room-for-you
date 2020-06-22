package com.mateusz.jakuszko.roomforyou.service;

import com.mateusz.jakuszko.roomforyou.domain.User;
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
public class UserDbServiceTest {

    @Autowired
    private UserDbService userDbService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private User createUser() {
        return User.builder()
                .name("Mateusz")
                .surname("Jakuszko")
                .username("matanos")
                .password("abc123")
                .role("Admin")
                .email("mateusz.jakuszko@gmail.com")
                .build();
    }

    @Test
    public void saveAndGetUserTest() {
        //Given
        User user = createUser();
        //When
        userDbService.save(user, passwordEncoder);
        Long userId = user.getId();
        Optional<User> savedUser = userDbService.getUser(userId);
        //Then
        assertNotNull(user.getId());
        assertTrue(savedUser.isPresent());
        assertEquals("Mateusz", savedUser.get().getName());
        assertEquals("Jakuszko", savedUser.get().getSurname());
        assertEquals("matanos", savedUser.get().getUsername());
        assertEquals("Admin", savedUser.get().getRole());
        assertEquals("mateusz.jakuszko@gmail.com", savedUser.get().getEmail());
        assertTrue(passwordEncoder.matches("abc123", savedUser.get().getPassword()));
    }

    @Test
    public void getUsersTest() {
        //Given
        User user1 = createUser();
        User user2 = createUser();
        userDbService.save(user1, passwordEncoder);
        userDbService.save(user2, passwordEncoder);
        //When
        List<User> users = userDbService.getUsers();
        //Then
        assertEquals(2, users.size());
        assertTrue(users.stream()
        .allMatch(user -> user.getId() != null &&
                user.getEmail().equals("mateusz.jakuszko@gmail.com") &&
                user.getName().equals("Mateusz") &&
                user.getSurname().equals("Jakuszko") &&
                user.getUsername().equals("matanos") &&
                user.getRole().equals("Admin") &&
                passwordEncoder.matches("abc123", user.getPassword())));
    }

    @Test
    public void updateTest() {
        //Given
        User user = createUser();
        //When
        userDbService.save(user, passwordEncoder);
        Long userId = user.getId();
        User userAfterSave = userDbService.getUser(userId).orElseThrow(NotFoundException::new);
        userAfterSave.setEmail("changedMail");
        userDbService.update(userAfterSave);
        Optional<User> userAfterUpdate = userDbService.getUser(userId);
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
    public void deleteTest() {
        //Given
        User user = createUser();
        //When
        userDbService.save(user, passwordEncoder);
        Long userId = user.getId();
        userDbService.delete(userId);
        Optional<User> deletedUser = userDbService.getUser(userId);
        //Then
        assertNotNull(user.getId());
        assertFalse(deletedUser.isPresent());
    }
}
