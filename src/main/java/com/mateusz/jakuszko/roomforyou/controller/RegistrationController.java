package com.mateusz.jakuszko.roomforyou.controller;

import com.mateusz.jakuszko.roomforyou.dto.UserDto;
import com.mateusz.jakuszko.roomforyou.facade.UserDbFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping
@Controller
public class RegistrationController {

    private final UserDbFacade userDbFacade;

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("user", new UserDto());
        return "sign-up";
    }

    @PostMapping("/register")
    public String register(UserDto user) {
        userDbFacade.createUser(user);
        return "sign-up";
    }
}
