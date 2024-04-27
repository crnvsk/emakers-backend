package com.emakers.biblioteca.dtos;

import jakarta.validation.constraints.NotBlank;

public record PersonRecordDTO(@NotBlank String name, @NotBlank String cep, @NotBlank String email, @NotBlank String password) {
}
