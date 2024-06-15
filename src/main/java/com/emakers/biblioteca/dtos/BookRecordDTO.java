package com.emakers.biblioteca.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BookRecordDTO(@NotBlank String title, @NotBlank String author, @NotNull LocalDate publishDate,
        Integer amount) {
}
