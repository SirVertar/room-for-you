package com.mateusz.jakuszko.roomforyou.exceptionhandler;

import com.mateusz.jakuszko.roomforyou.exceptions.PasswordIncorrectException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class PasswordIncorrectHandler {
    @ExceptionHandler(value = PasswordIncorrectException.class)
    @ResponseBody
    public String handleMethod(HttpServletRequest request, PasswordIncorrectException ex) {
        log.error("Request " + request.getRequestURL() + " Threw an Exception ", ex);
        return "Code 4. Incorrect password: to short or has no numbers inside";
    }
}
