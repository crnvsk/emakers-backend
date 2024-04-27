package com.emakers.biblioteca.exceptions;

public class BookIsBorrowedException extends RuntimeException {

    public BookIsBorrowedException(String message) {
        super(message);
    }
}
