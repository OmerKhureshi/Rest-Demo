package com.omer.restDemo;

import com.omer.restDemo.rest.model.Message;
import com.omer.restDemo.rest.model.Person;
import com.omer.restDemo.rest.repository.MessageRepository;
import com.omer.restDemo.rest.repository.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class  RestDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestDemoApplication.class, args);
	}

	@Bean
	CommandLineRunner init(PersonRepository personRepository, MessageRepository messageRepository) {
		return (args) -> {

            Person omer = new Person("Omer", "Khureshi");
            personRepository.save(omer);
            Person sai = new Person("Sai", "Undurthi");
            personRepository.save(sai);
            Person anshul = new Person("Anshul", "Vyas");
            personRepository.save(anshul);

            messageRepository.save(new Message(omer, sai, "omer to sai hello there"));
            messageRepository.save(new Message(omer, anshul, "omer to anshul hello there"));
            messageRepository.save(new Message(anshul, omer, "anshul to omer -> hello there"));
            messageRepository.save(new Message(anshul, sai, "anshul to sai -> hello there"));
            messageRepository.save(new Message(anshul, anshul, "anshul to anshul -> hello there"));
            messageRepository.save(new Message(sai, anshul, "sai to anshul -> hello there"));
        };
	}
}
