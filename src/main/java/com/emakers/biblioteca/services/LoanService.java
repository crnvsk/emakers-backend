package com.emakers.biblioteca.services;

import com.emakers.biblioteca.domain.book.Book;
import com.emakers.biblioteca.domain.loan.Loan;
import com.emakers.biblioteca.domain.person.Person;
import com.emakers.biblioteca.repositories.LoanRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final PersonService personService;

    public LoanService(LoanRepository loanRepository, BookService bookService, PersonService personService) {
        this.loanRepository = loanRepository;
        this.bookService = bookService;
        this.personService = personService;
    }

    public void saveLoan(Loan loan) {
        Book book = loan.getBook();
        book.setIsLoaned(true);
        bookService.updateBook(book);

        Person person = loan.getPerson();
        person.setIsBorrowing(true);
        personService.updatePerson(person);

        loanRepository.save(loan);
    }

    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    public Optional<Loan> getOneLoan(Integer loanId) {
        return loanRepository.findById(loanId);
    }

    public Loan updateLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    public void deleteLoan(Loan loan) {
        loanRepository.delete(loan);

        Book book = loan.getBook();
        if (!loanRepository.existsByBook(book)) {
            book.setIsLoaned(false);
            bookService.updateBook(book);
        }

        Person person = loan.getPerson();
        if (!loanRepository.existsByPerson(person)) {
            person.setIsBorrowing(false);
            personService.updatePerson(person);
        }
    }

    public Loan findLoanByBookAndPerson(Book book, Person person) {
        return loanRepository.findLoanByBookAndPerson(book, person);
    }
}
