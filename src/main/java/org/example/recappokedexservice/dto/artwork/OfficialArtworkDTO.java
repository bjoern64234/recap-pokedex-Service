package org.example.recappokedexservice.dto.artwork;

import jakarta.validation.constraints.*;

public record OfficialArtworkDTO(
        @NotBlank(message = "PictureUrl can not be blank.") String front_default,
        String front_shiny
) {
}
