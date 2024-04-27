package com.emakers.biblioteca.repositories;

import com.emakers.biblioteca.domain.loan.Loan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {
}