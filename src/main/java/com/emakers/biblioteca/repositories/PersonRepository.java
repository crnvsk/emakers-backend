package com.emakers.biblioteca.repositories;

import com.emakers.biblioteca.domain.person.Person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
}
