package com.emakers.biblioteca.controllers;

import com.emakers.biblioteca.domain.book.Book;
import com.emakers.biblioteca.domain.person.Person;
import com.emakers.biblioteca.domain.loan.Loan;
import com.emakers.biblioteca.services.BookService;
import com.emakers.biblioteca.services.PersonService;
import com.emakers.biblioteca.services.LoanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@Tag(name = "loan-rest-api")
public class LoanController {

    @Autowired
    private BookService bookService;

    @Autowired
    private PersonService personService;

    @Autowired
    private LoanService loanService;

    @Operation(summary = "Lend a book to someone", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book loaned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing parameters/information"),
            @ApiResponse(responseCode = "401", description = "Access denied due to invalid credentials"),
            @ApiResponse(responseCode = "403", description = "You don't have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Book or person not found"),
            @ApiResponse(responseCode = "409", description = "This loan already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server error")
    })
    @PostMapping(value = "/loan/{bookId}/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> loanBook(@PathVariable Integer bookId, @PathVariable Integer personId) {
        Book book = bookService.getOneBook(bookId).orElse(null);
        Person person = personService.getOnePerson(personId).orElse(null);

        if (book == null || person == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book or Person not found.");
        }

        Loan existingLoan = loanService.findLoanByBookAndPerson(book, person);
        if (existingLoan != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book already borrowed by this person.");
        }

        if (book.getAmount() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book is not available for loan.");
        }

        book.setAmount(book.getAmount() - 1);
        bookService.updateBook(book);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setPerson(person);
        loanService.saveLoan(loan);

        return ResponseEntity.status(HttpStatus.OK).body("Book successfully borrowed!");
    }

    @Operation(summary = "Return a book to the library", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing parameters/information"),
            @ApiResponse(responseCode = "401", description = "Access denied due to invalid credentials"),
            @ApiResponse(responseCode = "403", description = "You don't have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Book or person not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server error")
    })
    @PostMapping(value = "/return/{bookId}/{personId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> returnBook(@PathVariable Integer bookId, @PathVariable Integer personId) {
        Book book = bookService.getOneBook(bookId).orElse(null);
        Person person = personService.getOnePerson(personId).orElse(null);

        if (book == null || person == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book or Person not found.");
        }

        Loan loan = loanService.findLoanByBookAndPerson(book, person);
        if (loan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan not found.");
        }

        book.setAmount(book.getAmount() + 1);
        bookService.updateBook(book);

        loanService.deleteLoan(loan);

        return ResponseEntity.status(HttpStatus.OK).body("Book successfully returned!");
    }
}
