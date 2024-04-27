package com.emakers.biblioteca.controllers;

import com.emakers.biblioteca.domain.person.Person;
import com.emakers.biblioteca.dtos.PersonRecordDTO;
import com.emakers.biblioteca.services.PersonService;

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
    private PersonService personService;

    @PostMapping("/persons")
    public ResponseEntity<Person> savePerson(@RequestBody @Valid PersonRecordDTO personRecordDTO) {
        Person person = new Person();

        BeanUtils.copyProperties(personRecordDTO, person);
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.savePerson(person));
    }

    @GetMapping("/persons")
    public ResponseEntity<List<Person>> getAllPersons() {
        List<Person> personList = personService.getAllPersons();
        if (!personList.isEmpty()) {
            for (Person person : personList) {
                Integer personId = person.getPersonId();
                person.add(linkTo(methodOn(PersonController.class).getOnePerson(personId)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(personList);
    }

    @GetMapping("/persons/{personId}")
    public ResponseEntity<Object> getOnePerson(@PathVariable(value ="personId") Integer personId) {
        Optional<Person> person = personService.getOnePerson(personId);
        if(person.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }

        person.get().add(linkTo(methodOn(PersonController.class).getAllPersons()).withRel("Persons List"));
        return ResponseEntity.status(HttpStatus.OK).body(person.get());
    }

    @PutMapping("/persons/{personId}")
    public ResponseEntity<Object> updatePerson(@PathVariable(value ="personId") Integer personId, @RequestBody @Valid PersonRecordDTO personRecordDTO) {
        Optional<Person> existingPersonOptional = personService.getOnePerson(personId);
        if(existingPersonOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }
        Person existingPerson = existingPersonOptional.get();

        BeanUtils.copyProperties(personRecordDTO, existingPerson);
        return ResponseEntity.status(HttpStatus.OK).body(personService.updatePerson(existingPerson));
    }

    @DeleteMapping("/persons/{personId}")
    public ResponseEntity<Object> deletePerson(@PathVariable(value ="personId") Integer personId) {
        Optional<Person> personToDeleteOptional = personService.getOnePerson(personId);
        if(personToDeleteOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }
        personService.deletePerson(personToDeleteOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Person deleted successfully.");
    }
}
