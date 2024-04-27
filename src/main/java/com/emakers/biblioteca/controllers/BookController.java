package com.emakers.biblioteca.controllers;

import com.emakers.biblioteca.domain.book.Book;
import com.emakers.biblioteca.dtos.BookRecordDTO;
import com.emakers.biblioteca.repositories.BookRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
