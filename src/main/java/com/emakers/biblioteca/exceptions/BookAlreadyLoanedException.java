package com.emakers.biblioteca.exceptions;

public class BookAlreadyLoanedException extends RuntimeException {

    public BookAlreadyLoanedException(String message) {
        super(message);
    }
}
