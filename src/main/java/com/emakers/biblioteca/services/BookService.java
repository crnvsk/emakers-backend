package com.emakers.biblioteca.services;

import com.emakers.biblioteca.domain.book.Book;
import com.emakers.biblioteca.exceptions.BookAlreadyLoanedException;
import com.emakers.biblioteca.exceptions.DuplicateEntityException;
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
        Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor());
        if (existingBook.isPresent()) {
            throw new DuplicateEntityException("This book already exists.");
        }
        book.setIsLoaned(false);
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getOneBook(Integer bookId) {
        return bookRepository.findById(bookId);
    }

    public Book updateBook(Book book) {
        Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor());
        if (existingBook.isPresent() && !existingBook.get().getBookId().equals(book.getBookId())) {
            throw new DuplicateEntityException("Another book with the same title and author already exists.");
        }
        return bookRepository.save(book);
    }


    public void deleteBook(Book book) {
        boolean bookO = book.getIsLoaned();
        if(bookO){
            throw new BookAlreadyLoanedException("Cannot delete book because it is currently loaned.");
        }
        bookRepository.delete(book);
    }

    public Optional<Book> getBookByTitleAndAuthor(String title, String author) {
        return bookRepository.findByTitleAndAuthor(title, author);
    }
}
