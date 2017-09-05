package com.omer.restDemo.rest.controller;

import com.omer.restDemo.rest.exception.MessageNotFoundException;
import com.omer.restDemo.rest.exception.PersonNotFoundException;
import com.omer.restDemo.rest.model.Message;
import com.omer.restDemo.rest.model.MessageResource;
import com.omer.restDemo.rest.repository.MessageRepository;
import com.omer.restDemo.rest.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    PersonController personController;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResource createMessage(@RequestBody Message message) {
        personController.validatePerson(message.getSender().getId());
        personController.validatePerson(message.getReceiver().getId());
        messageRepository.save(message);
        return new MessageResource(message);
    }

    @RequestMapping(value = "/{messageId}", method = RequestMethod.GET)
    public MessageResource getMessage(@PathVariable Long messageId) {
        validateMessage(messageId);
        return new MessageResource(messageRepository.findOne(messageId));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MessageResource> getMessages() {
        List<MessageResource> list = ((List<Message>)messageRepository.findAll())
                .stream().map(MessageResource::new)
                .collect(Collectors.toList());
        return list;
    }

    @RequestMapping(params = "senderId", method = RequestMethod.GET)
    public Collection<MessageResource> getMessagesFrom(@RequestParam(value = "senderId") Long senderId) {
        personController.validatePerson(senderId);
        return messageRepository.findBySenderId(senderId)
                .stream()
                .map(MessageResource::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(params = "receiverId", method = RequestMethod.GET)
    public Collection<MessageResource> getMessagesTo(@RequestParam(value = "receiverId") Long receiverId) {
        personController.validatePerson(receiverId);
        return messageRepository.findByReceiverId(receiverId)
                .stream()
                .map(MessageResource::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(params = {"senderId", "receiverId"}, method = RequestMethod.GET)
    public Collection<MessageResource> getMessagesFromAndTo(@RequestParam(value = "senderId") Long senderId,
                                                            @RequestParam(value = "receiverId") Long receiverId) {
        personController.validatePerson(senderId);
        personController.validatePerson(receiverId);

        return ((List<Message>) messageRepository.findBySenderIdAndReceiverId(senderId, receiverId))
                .stream()
                .map(MessageResource::new)
                .collect(Collectors.toList());
    }

    public void validateMessage(Long id) {
        if (messageRepository.findOne(id) == null) {
            throw new MessageNotFoundException(id);
        }
    }

}
