package com.mateusz.jakuszko.roomforyou.exceptionhandler;

import com.mateusz.jakuszko.roomforyou.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class NotFoundHandler {
    @ExceptionHandler(value = NotFoundException.class)
    @ResponseBody
    public String handleMethod(HttpServletRequest request, NotFoundException ex) {
        log.error("Request " + request.getRequestURL() + " threw an Exception:", ex);
        return "Code 2. There was a problem to find an object in database, probably server has gotten wrong id of Entity";
    }
}
