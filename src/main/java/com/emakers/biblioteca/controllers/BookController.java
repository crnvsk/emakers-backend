package com.emakers.biblioteca.controllers;

import com.emakers.biblioteca.domain.book.Book;
import com.emakers.biblioteca.dtos.BookRecordDTO;
import com.emakers.biblioteca.services.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api")
@Tag(name = "book-rest-api")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "Saves a book", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing parameters/information"),
            @ApiResponse(responseCode = "401", description = "Access denied due to invalid credentials"),
            @ApiResponse(responseCode = "403", description = "You don't have permission to access this resource"),
            @ApiResponse(responseCode = "409", description = "This book already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server error")
    })
    @PostMapping(value = "/books/saveBook", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> saveBook(@RequestBody @Valid BookRecordDTO bookRecordDTO) {
        Book book = new Book();
        BeanUtils.copyProperties(bookRecordDTO, book);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.saveBook(book));
    }

    @Operation(summary = "Search for all books", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing parameters/information"),
            @ApiResponse(responseCode = "401", description = "Access denied due to invalid credentials"),
            @ApiResponse(responseCode = "403", description = "You don't have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "422", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal Server error")
    })
    @GetMapping(value = "/books/getAllBooks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> booksList = bookService.getAllBooks();
        if (!booksList.isEmpty()) {
            for (Book book : booksList) {
                Integer bookId = book.getBookId();
                book.add(linkTo(methodOn(BookController.class).getOneBook(bookId)).withSelfRel());
            }
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.status(HttpStatus.OK).body(booksList);
    }

    @Operation(summary = "Search for one specific books", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing parameters/information"),
            @ApiResponse(responseCode = "401", description = "Access denied due to invalid credentials"),
            @ApiResponse(responseCode = "403", description = "You don't have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "422", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal Server error")
    })
    @GetMapping(value = "/books/getOneBook/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getOneBook(@PathVariable(value ="bookId") Integer bookId) {
        Optional<Book> book = bookService.getOneBook(bookId);
        if(book.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
        book.get().add(linkTo(methodOn(BookController.class).getAllBooks()).withRel("Books List"));
        return ResponseEntity.status(HttpStatus.OK).body(book.get());
    }

    @Operation(summary = "Updates a book's data", method = "UPDATE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book saved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing parameters/information"),
            @ApiResponse(responseCode = "401", description = "Access denied due to invalid credentials"),
            @ApiResponse(responseCode = "403", description = "You don't have permission to access this resource"),
            @ApiResponse(responseCode = "409", description = "This book already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server error")
    })
    @PutMapping(value = "/books/updateBook/{bookId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateBook(@PathVariable(value ="bookId") Integer bookId, @RequestBody @Valid BookRecordDTO bookRecordDTO) {
        Optional<Book> existingBookOptional = bookService.getOneBook(bookId);
        if(existingBookOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }

        Book existingBook = existingBookOptional.get();

        Optional<Book> anotherBookOptional = bookService.getBookByTitleAndAuthor(bookRecordDTO.title(), bookRecordDTO.author());
        if (anotherBookOptional.isPresent() && !anotherBookOptional.get().getBookId().equals(bookId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Another book with the same title and author already exists.");
        }
        BeanUtils.copyProperties(bookRecordDTO, existingBook);
        return ResponseEntity.status(HttpStatus.OK).body(bookService.updateBook(existingBook));
    }

    @Operation(summary = "Deletes a book", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing parameters/information"),
            @ApiResponse(responseCode = "401", description = "Access denied due to invalid credentials"),
            @ApiResponse(responseCode = "403", description = "You don't have permission to access this resource"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server error")
    })
    @DeleteMapping(value = "/books/deleteBook/{bookId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteBook(@PathVariable(value ="bookId") Integer bookId) {
        Optional<Book> bookToDeleteOptional = bookService.getOneBook(bookId);
        if(bookToDeleteOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
        }
        bookService.deleteBook(bookToDeleteOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Book deleted successfully.");
    }
}
