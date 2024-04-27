package com.emakers.biblioteca.repositories;

import com.emakers.biblioteca.domain.person.Person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByNameAndEmail(String name, String email);
}
