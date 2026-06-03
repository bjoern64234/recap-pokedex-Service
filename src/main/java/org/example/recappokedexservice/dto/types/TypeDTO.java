package org.example.recappokedexservice.dto.types;

import jakarta.validation.constraints.*;

public record TypeDTO(
        @NotBlank(message = "The type can not be blank.") String name,
        String url
) {
}
