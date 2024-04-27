package com.emakers.biblioteca.dtos;

import com.emakers.biblioteca.domain.book.Book;
import com.emakers.biblioteca.domain.person.Person;
import jakarta.validation.constraints.NotNull;

public record LoanRecordDTO(@NotNull Book bookId, @NotNull Person personId) {
}
