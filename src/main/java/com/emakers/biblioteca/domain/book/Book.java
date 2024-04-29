package com.emakers.biblioteca.domain.book;

import jakarta.persistence.*;

import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

import java.time.LocalDate;

@Entity
@Table(name = "book")
public class Book extends RepresentationModel<Book> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;

    @Column(length = 45, nullable = false)
    private String title;

    @Column(length = 45, nullable = false)
    private String author;

    @Column(nullable = false)
    private LocalDate publishDate;

    @Column(nullable = false)
    private Integer amount;

    private Boolean isLoaned;

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Boolean getIsLoaned() {
        return isLoaned;
    }

    public void setIsLoaned(Boolean loaned) {
        isLoaned = loaned;
    }
}
