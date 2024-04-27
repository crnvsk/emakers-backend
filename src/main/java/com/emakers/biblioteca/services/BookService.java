package com.emakers.biblioteca.services;

import com.emakers.biblioteca.domain.book.Book;
import com.emakers.biblioteca.repositories.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getOneBook(Integer bookId) {
        return bookRepository.findById(bookId);
    }

    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Book book) {
        bookRepository.delete(book);
    }
}
