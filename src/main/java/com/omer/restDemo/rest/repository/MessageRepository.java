package com.omer.restDemo.rest.repository;

import com.omer.restDemo.rest.model.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface MessageRepository extends CrudRepository<Message, Long> {
    Collection<Message> findBySenderId(Long id);
    Collection<Message> findByReceiverId(Long id);
    Collection<Message> findBySenderIdAndReceiverId(Long senderId, Long receiverId);

}
