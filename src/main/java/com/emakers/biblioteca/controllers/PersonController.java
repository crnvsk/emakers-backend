package com.emakers.biblioteca.controllers;

import com.emakers.biblioteca.domain.person.Person;
import com.emakers.biblioteca.dtos.PersonRecordDTO;
import com.emakers.biblioteca.repositories.PersonRepository;

import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @PostMapping("/persons")
    public ResponseEntity<Person> savePerson(@RequestBody @Valid PersonRecordDTO personRecordDTO) {
        var newPerson = new Person();
        BeanUtils.copyProperties(personRecordDTO, newPerson);
        return ResponseEntity.status(HttpStatus.CREATED).body(personRepository.save(newPerson));
    }

    @GetMapping("/persons")
    public ResponseEntity<List<Person>> getAllPersons() {
        List<Person> personList = personRepository.findAll();
        if (!personList.isEmpty()) {
            for (Person person : personList) {
                Integer personId = person.getPersonId();
                person.add(linkTo(methodOn(PersonController.class).getOnePerson(personId)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(personRepository.findAll());
    }

    @GetMapping("/persons/{personId}")
    public ResponseEntity<Object> getOnePerson(@PathVariable(value ="personId") Integer personId) {
        Optional<Person> personO = personRepository.findById(personId);
        if(personO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }
        personO.get().add(linkTo(methodOn(PersonController.class).getAllPersons()).withRel("Persons List"));
        return ResponseEntity.status(HttpStatus.OK).body(personO.get());
    }

    @PutMapping("/persons/{personId}")
    public ResponseEntity<Object> updatePerson(@PathVariable(value ="personId") Integer personId, @RequestBody @Valid PersonRecordDTO personRecordDTO) {
        Optional<Person> personO = personRepository.findById(personId);
        if(personO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }
        var person = personO.get();
        BeanUtils.copyProperties(personRecordDTO, person);
        return ResponseEntity.status(HttpStatus.OK).body(personRepository.save(person));
    }

    @DeleteMapping("/persons/{personId}")
    public ResponseEntity<Object> deletePerson(@PathVariable(value ="personId") Integer personId) {
        Optional<Person> personO = personRepository.findById(personId);
        if(personO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }
        personRepository.delete(personO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Person deleted successfully.");
    }
}
