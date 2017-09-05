package com.omer.restDemo.rest.model;

import com.omer.restDemo.rest.controller.MessageController;
import com.omer.restDemo.rest.controller.PersonController;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class PersonResource extends ResourceSupport {

    private Person person;

    public PersonResource(Person person) {
        this.person = person;

        // self link
        this.add(linkTo(methodOn(PersonController.class)
                .getPerson(person.getId())).withSelfRel());

        // Link to get all messages sent by this person
        this.add(linkTo(methodOn(MessageController.class)
                .getMessagesFrom(person.getId()))
                .withRel("from-messages"));

        // Link to get all messages received by this person
        this.add(linkTo(methodOn(MessageController.class)
                .getMessagesTo(person.getId()))
                .withRel("to-messages"));
    }

    public Person getPerson() {
        return person;
    }
}
