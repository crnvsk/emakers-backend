package com.emakers.biblioteca.exceptions;

public class PersonHasBooksBorrowedException extends RuntimeException {
    public PersonHasBooksBorrowedException(String message) {
        super(message);
    }
}
