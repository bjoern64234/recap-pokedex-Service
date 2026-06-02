package org.example.recappokedexservice.dto;

import jakarta.validation.constraints.*;
import org.example.recappokedexservice.dto.artwork.SpritesDTO;
import org.example.recappokedexservice.dto.types.TypesDTO;

import java.util.List;

public record PokemonResponse(
        @NotNull(message = "The pokemonId cannot be null.") int id,
        @NotBlank(message = "The username cannot be blank.") String name,
        @Positive(message = "The height must be a positive number.") int height,
        @Positive(message = "The weight must be a positive number.") int weight,
        SpritesDTO sprites,
        List<TypesDTO> types
) {
}
