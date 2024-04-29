package com.emakers.biblioteca.repositories;

import com.emakers.biblioteca.domain.book.Book;
import com.emakers.biblioteca.domain.loan.Loan;
import com.emakers.biblioteca.domain.person.Person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {
    Loan findLoanByBookAndPerson(Book book, Person person);

    boolean existsByBook(Book book);

    boolean existsByPerson(Person person);
}