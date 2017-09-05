package com.omer.restDemo.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @NotNull
    private Person sender;

    @ManyToOne
    @NotNull
    private Person receiver;

    private String messageDesc;

    Message(){}

    public Message(Person sender, Person receiver, String messageDesc) {
        this.sender = sender;
        this.receiver = receiver;

        // this.senderPersonId = sender.getId();
        // this.receiverPersonId = receiver.getId();

        this.messageDesc = messageDesc;
    }

    // public Long getSenderPersonId() {
    //     return senderPersonId;
    // }
    //
    // public Long getReceiverPersonId() {
    //     return receiverPersonId;
    // }


    public Long getId() {
        return id;
    }

    public Person getSender() {
        return sender;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

    public Person getReceiver() {
        return receiver;
    }

    public void setReceiver(Person receiver) {
        this.receiver = receiver;
    }


    public String getMessageDesc() {
        return messageDesc;
    }

    public void setMessageDesc(String messageDesc) {
        this.messageDesc = messageDesc;
    }
}
