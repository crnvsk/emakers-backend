package com.emakers.biblioteca.controllers;

import com.emakers.biblioteca.domain.book.Book;
import com.emakers.biblioteca.dtos.BookRecordDTO;
import com.emakers.biblioteca.services.BookService;

import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping("/books")
    public ResponseEntity<Book> saveBook(@RequestBody @Valid BookRecordDTO bookRecordDTO) {
        Book book = new Book();
        BeanUtils.copyProperties(bookRecordDTO, book);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.saveBook(book));
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> booksList = bookService.getAllBooks();
        if (!booksList.isEmpty()) {
            for (Book book : booksList) {
                Integer bookId = book.getBookId();
                book.add(linkTo(methodOn(BookController.class).getOneBook(bookId)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(booksList);
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<Object> getOneBook(@PathVariable(value ="bookId") Integer bookId) {
        Optional<Book> book = bookService.getOneBook(bookId);
        if(book.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
        book.get().add(linkTo(methodOn(BookController.class).getAllBooks()).withRel("Books List"));
        return ResponseEntity.status(HttpStatus.OK).body(book.get());
    }

    @PutMapping("/books/{bookId}")
    public ResponseEntity<Object> updateBook(@PathVariable(value ="bookId") Integer bookId, @RequestBody @Valid BookRecordDTO bookRecordDTO) {
        Optional<Book> existingBookOptional = bookService.getOneBook(bookId);
        if(existingBookOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
        Book existingBook = existingBookOptional.get();
        BeanUtils.copyProperties(bookRecordDTO, existingBook);
        return ResponseEntity.status(HttpStatus.OK).body(bookService.updateBook(existingBook));
    }

    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<Object> deleteBook(@PathVariable(value ="bookId") Integer bookId) {
        Optional<Book> bookToDeleteOptional = bookService.getOneBook(bookId);
        if(bookToDeleteOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
        bookService.deleteBook(bookToDeleteOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Book deleted successfully.");
    }
}
