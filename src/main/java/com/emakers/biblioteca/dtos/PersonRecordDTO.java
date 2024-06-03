package com.emakers.biblioteca.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PersonRecordDTO(@NotBlank String name, @NotBlank String cep, @NotBlank String email, @NotBlank String password, @NotNull Boolean isAdmin) {
}
