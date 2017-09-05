package com.omer.restDemo.rest.controller;

import com.omer.restDemo.rest.exception.PersonNotFoundException;
import com.omer.restDemo.rest.model.Person;
import com.omer.restDemo.rest.model.PersonResource;
import com.omer.restDemo.rest.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<PersonResource> getPersons() {
        return ((List<Person>) personRepository.findAll())
                .stream()
                .map(PersonResource::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public PersonResource createPerson(@RequestBody Person person) {
        personRepository.save(person);
        return new PersonResource((person));
    }

    @RequestMapping(value = "/{personId}", method = RequestMethod.GET)
    public PersonResource getPerson(@PathVariable Long personId) {
        validatePerson(personId);
        return new PersonResource(personRepository.findOne(personId));
    }


    public void validatePerson(Long id) {
        if (personRepository.findOne(id) == null) {
            throw new PersonNotFoundException(id);
        }
    }


}
