package com.emakers.biblioteca.services;

import com.emakers.biblioteca.domain.person.Person;
import com.emakers.biblioteca.exceptions.DuplicateEntityException;
import com.emakers.biblioteca.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person savePerson(Person person) {
        Optional<Person> existingPerson = personRepository.findByNameAndEmail(person.getName(), person.getEmail());
        if (existingPerson.isPresent()) {
            throw new DuplicateEntityException("This person already exists.");
        }
        return personRepository.save(person);
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Optional<Person> getOnePerson(Integer personId) {
        return personRepository.findById(personId);
    }

    public Person updatePerson(Person person) {
        return personRepository.save(person);
    }

    public void deletePerson(Person person) {
        personRepository.delete(person);
    }
}
