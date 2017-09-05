package com.omer.restDemo.rest.model;

import com.omer.restDemo.rest.controller.MessageController;
import com.omer.restDemo.rest.controller.PersonController;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class MessageResource extends ResourceSupport {

    private Message message;

    public MessageResource(Message message) {
        this.message = message;

        // Self link.
        this.add(linkTo(methodOn(MessageController.class)
                .getMessage(message.getId()))
                .withSelfRel());

        // Link to Sender of this message.
        this.add(linkTo(methodOn(PersonController.class)
                .getPerson(message.getSender().getId()))
                .withRel("sender"));

        // Link to Receiver of this message.
        this.add(linkTo(methodOn(PersonController.class)
                .getPerson(message.getReceiver().getId()))
                .withRel("receiver"));
    }

    public Message getMessage() {
        return message;
    }
}
