package com.mateusz.jakuszko.roomforyou.controller;

import com.mateusz.jakuszko.roomforyou.domain.UserDto;
import com.mateusz.jakuszko.roomforyou.facade.UserDbFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/users")
public class UserController {

    private final UserDbFacade userDbFacade;

    @GetMapping
    public List<UserDto> get() {
        return userDbFacade.getUsers();
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        return userDbFacade.getUser(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDto create(@RequestBody UserDto userDto) {
        return userDbFacade.createUser(userDto);
    }

    @PutMapping
    public UserDto update(@RequestBody UserDto userDto) {
        return userDbFacade.createUser(userDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userDbFacade.deleteUser(id);
    }
}
