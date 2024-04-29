package com.emakers.biblioteca.controllers;

import com.emakers.biblioteca.domain.book.Book;
import com.emakers.biblioteca.domain.person.Person;
import com.emakers.biblioteca.domain.loan.Loan;
import com.emakers.biblioteca.services.BookService;
import com.emakers.biblioteca.services.PersonService;
import com.emakers.biblioteca.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class LoanController {

    @Autowired
    private BookService bookService;

    @Autowired
    private PersonService personService;

    @Autowired
    private LoanService loanService;

    @PostMapping("/loan/{bookId}/{personId}")
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


    @PostMapping("/return/{bookId}/{personId}")
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
