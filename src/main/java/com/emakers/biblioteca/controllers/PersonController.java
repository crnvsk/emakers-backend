package com.emakers.biblioteca.controllers;

import com.emakers.biblioteca.domain.person.Person;
import com.emakers.biblioteca.dtos.PersonRecordDTO;
import com.emakers.biblioteca.repositories.PersonRepository;

import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
