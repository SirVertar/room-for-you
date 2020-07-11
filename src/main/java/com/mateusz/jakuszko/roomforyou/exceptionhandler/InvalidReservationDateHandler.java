package com.mateusz.jakuszko.roomforyou.exceptionhandler;

import com.mateusz.jakuszko.roomforyou.exceptions.InvalidReservationDateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class InvalidReservationDateHandler {
    @ExceptionHandler(value = InvalidReservationDateException.class)
    @ResponseBody
    public String handleMethod(HttpServletRequest request, InvalidReservationDateException ex) {
        log.error("Request " + request.getRequestURL() + " threw an Exception:", ex);
        return "Code 3. In database exists reservation of this apartment which dates are within the provided range";
    }
}
