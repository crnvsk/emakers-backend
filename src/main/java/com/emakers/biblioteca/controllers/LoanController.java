package com.emakers.biblioteca.controllers;

import com.emakers.biblioteca.domain.book.Book;
import com.emakers.biblioteca.domain.loan.Loan;
import com.emakers.biblioteca.domain.person.Person;
import com.emakers.biblioteca.repositories.BookRepository;
import com.emakers.biblioteca.repositories.LoanRepository;
import com.emakers.biblioteca.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class LoanController {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PersonRepository personRepository;

    @PostMapping("/loan/{bookId}/{personId}")
    public ResponseEntity<String> loanBook(@PathVariable Integer bookId, @PathVariable Integer personId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        Optional<Person> personOptional = personRepository.findById(personId);

        if (bookOptional.isPresent() && personOptional.isPresent()) {
            Book book = bookOptional.get();
            Person person = personOptional.get();

            if (book.getAmount() > 0) {
                Loan loan = new Loan();
                loan.setBook(book);
                loan.setPerson(person);

                book.setAmount(book.getAmount() - 1);
                bookRepository.save(book);

                loanRepository.save(loan);

                return new ResponseEntity<>("Book successfully borrowed!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Book is not available for loan.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Book or Person not found.", HttpStatus.NOT_FOUND);
        }
    }

    /*@PostMapping("/return/{bookId}/{personId}")
    public ResponseEntity<String> returnBook(@PathVariable Integer bookId, @PathVariable Integer personId) {
        Optional<Loan> loanOptional = loanRepository.findByBookIdBookAndPersonIdPerson(bookId, personId);

        if (loanOptional.isPresent()) {
            Loan loan = loanOptional.get();
            loanRepository.delete(loan);

            Book book = loan.getBook();
            book.setAmount(book.getAmount() + 1);
            bookRepository.save(book);

            return new ResponseEntity<>("Book returned successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Loan not found.", HttpStatus.NOT_FOUND);
        }
    }*/
}
