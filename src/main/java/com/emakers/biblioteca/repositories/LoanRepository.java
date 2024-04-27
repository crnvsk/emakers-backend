package com.emakers.biblioteca.repositories;

import com.emakers.biblioteca.domain.loan.Loan;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Integer> {
}
