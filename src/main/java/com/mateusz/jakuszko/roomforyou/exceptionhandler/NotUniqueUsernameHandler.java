package com.mateusz.jakuszko.roomforyou.exceptionhandler;

import com.mateusz.jakuszko.roomforyou.exceptions.NotUniqueUsernameException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class NotUniqueUsernameHandler {
    @ExceptionHandler(value = NotUniqueUsernameException.class)
    @ResponseBody
    public String handleMethod(HttpServletRequest request, NotUniqueUsernameException ex) {
        log.error("Request " + request.getRequestURL() + " Threw an Exception ", ex);
        return "Code 1. This is not unique username, you should change it.";
    }
}
