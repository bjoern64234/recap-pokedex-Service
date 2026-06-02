package org.example.recappokedexservice.dto.artwork;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OtherDTO(@JsonProperty("official-artwork") OfficialArtworkDTO official_artwork) {
}
