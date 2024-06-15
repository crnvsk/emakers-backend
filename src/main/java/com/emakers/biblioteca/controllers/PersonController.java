package com.emakers.biblioteca.controllers;

import com.emakers.biblioteca.domain.person.Person;
import com.emakers.biblioteca.dtos.PersonRecordDTO;
import com.emakers.biblioteca.services.PersonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api")
@Tag(name = "person-rest-api")
public class PersonController {

    @Autowired
    private PersonService personService;

    @Operation(summary = "Saves a person", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing parameters/information"),
            @ApiResponse(responseCode = "401", description = "Access denied due to invalid credentials"),
            @ApiResponse(responseCode = "403", description = "You don't have permission to access this resource"),
            @ApiResponse(responseCode = "409", description = "This person already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server error")
    })
    @PostMapping(value = "/persons/savePerson", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> savePerson(@RequestBody @Valid PersonRecordDTO personRecordDTO) {
        Person person = new Person();
        BeanUtils.copyProperties(personRecordDTO, person);
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.savePerson(person));
    }

    @Operation(summary = "Search for all persons", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing parameters/information"),
            @ApiResponse(responseCode = "401", description = "Access denied due to invalid credentials"),
            @ApiResponse(responseCode = "403", description = "You don't have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "422", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal Server error")
    })
    @GetMapping(value = "/persons/getAllPersons", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Person>> getAllPersons() {
        List<Person> personList = personService.getAllPersons();
        if (!personList.isEmpty()) {
            for (Person person : personList) {
                Integer personId = person.getPersonId();
                person.add(linkTo(methodOn(PersonController.class).getOnePerson(personId)).withSelfRel());
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.status(HttpStatus.OK).body(personList);
    }

    @Operation(summary = "Search for one specific person", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing parameters/information"),
            @ApiResponse(responseCode = "401", description = "Access denied due to invalid credentials"),
            @ApiResponse(responseCode = "403", description = "You don't have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "422", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal Server error")
    })
    @GetMapping(value = "/persons/getOnePerson/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getOnePerson(@PathVariable(value = "personId") Integer personId) {
        Optional<Person> person = personService.getOnePerson(personId);
        if (person.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }

        person.get().add(linkTo(methodOn(PersonController.class).getAllPersons()).withRel("Persons List"));
        return ResponseEntity.status(HttpStatus.OK).body(person.get());
    }

    @Operation(summary = "Updates a person's data", method = "UPDATE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing parameters/information"),
            @ApiResponse(responseCode = "401", description = "Access denied due to invalid credentials"),
            @ApiResponse(responseCode = "403", description = "You don't have permission to access this resource"),
            @ApiResponse(responseCode = "409", description = "This person already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server error")
    })
    @PutMapping(value = "/persons/updatePerson/{personId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updatePerson(@PathVariable(value = "personId") Integer personId,
            @RequestBody @Valid PersonRecordDTO personRecordDTO) {
        Optional<Person> existingPersonOptional = personService.getOnePerson(personId);
        if (existingPersonOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }
        Person existingPerson = existingPersonOptional.get();

        BeanUtils.copyProperties(personRecordDTO, existingPerson);
        return ResponseEntity.status(HttpStatus.OK).body(personService.updatePerson(existingPerson));
    }

    @Operation(summary = "Deletes a person", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing parameters/information"),
            @ApiResponse(responseCode = "401", description = "Access denied due to invalid credentials"),
            @ApiResponse(responseCode = "403", description = "You don't have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server error")
    })
    @DeleteMapping(value = "/persons/deletePerson/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deletePerson(@PathVariable(value = "personId") Integer personId) {
        Optional<Person> personToDeleteOptional = personService.getOnePerson(personId);
        if (personToDeleteOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Person not found.");
        }
        personService.deletePerson(personToDeleteOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Person deleted successfully.");
    }
}
