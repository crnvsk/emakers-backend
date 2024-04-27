package com.emakers.biblioteca.controllers;

import com.emakers.biblioteca.domain.book.Book;
import com.emakers.biblioteca.dtos.BookRecordDTO;
import com.emakers.biblioteca.repositories.BookRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @PostMapping("/books")
    public ResponseEntity<Book> saveBook(@RequestBody @Valid BookRecordDTO bookRecordDTO) {
        var newBook = new Book();
        BeanUtils.copyProperties(bookRecordDTO, newBook);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookRepository.save(newBook));
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(bookRepository.findAll());
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<Object> getOneBook(@PathVariable(value ="bookId") Integer bookId) {
        Optional<Book> personO = bookRepository.findById(bookId);
        if(personO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(personO.get());
    }

    @PutMapping("/books/{bookId}")
    public ResponseEntity<Object> updateBook(@PathVariable(value ="bookId") Integer bookId, @RequestBody @Valid BookRecordDTO bookRecordDTO) {
        Optional<Book> book0 = bookRepository.findById(bookId);
        if(book0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
        var book = book0.get();
        BeanUtils.copyProperties(bookRecordDTO, book);
        return ResponseEntity.status(HttpStatus.OK).body(bookRepository.save(book));
    }

    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<Object> deleteBook(@PathVariable(value ="bookId") Integer bookId) {
        Optional<Book> book0 = bookRepository.findById(bookId);
        if(book0.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
        bookRepository.delete(book0.get());
        return ResponseEntity.status(HttpStatus.OK).body("Book deleted successfully.");
    }
}
