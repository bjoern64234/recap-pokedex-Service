package org.example.recappokedexservice.dto.client;

import jakarta.validation.constraints.*;

public record FavoriteDTO(
        @Size(min = 3, message= "The pokemon name must be grater than tree characters.") String pokemonName,
        @Size(min = 2, message= "The nickname must at least more than one character.") String nickname
) {
}
