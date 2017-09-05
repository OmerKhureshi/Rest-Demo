package com.omer.restDemo.rest.repository;

import com.omer.restDemo.rest.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {
}
