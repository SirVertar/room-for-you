package com.mateusz.jakuszko.roomforyou.controller;

import com.mateusz.jakuszko.roomforyou.dto.CustomerDto;
import com.mateusz.jakuszko.roomforyou.facade.CustomerDbFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping
@Controller
public class RegistrationController {

    private final CustomerDbFacade customerDbFacade;

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("user", new CustomerDto());
        return "sign-up";
    }

    @PostMapping("/register")
    public String register(CustomerDto user) {
        customerDbFacade.createCustomer(user);
        return "sign-up";
    }
}
