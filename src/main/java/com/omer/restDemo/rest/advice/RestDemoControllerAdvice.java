package com.omer.restDemo.rest.advice;

import com.omer.restDemo.rest.exception.MessageNotFoundException;
import com.omer.restDemo.rest.exception.PersonNotFoundException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestDemoControllerAdvice {

    @ResponseBody
    @ExceptionHandler(PersonNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    VndErrors PersonNotFoundExceptionHandler(PersonNotFoundException ex) {
        return new VndErrors("Error", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    VndErrors MessageNotFoundExceptionHandler(MessageNotFoundException ex) {
        return new VndErrors("Error", ex.getMessage());
    }

}
