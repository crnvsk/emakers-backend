package com.emakers.biblioteca.repositories;

import com.emakers.biblioteca.domain.person.Person;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
}
