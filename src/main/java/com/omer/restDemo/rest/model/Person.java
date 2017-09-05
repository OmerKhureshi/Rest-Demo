package com.omer.restDemo.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.omer.restDemo.rest.controller.PersonController;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;


@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String firstName;

    String lastName;

    @OneToMany(mappedBy = "sender")
    List<Message> senxtMessages;

    @OneToMany(mappedBy = "receiver")
    List<Message> receivedMessages;

    Person(){
    }

    public Person(String firstName, String lastName) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
