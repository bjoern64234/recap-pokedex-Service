package org.example.recappokedexservice.model;

import lombok.Builder;
import lombok.With;
import org.example.recappokedexservice.dto.types.TypesDTO;

import java.util.List;

@With
@Builder
public record Pokemon(String id, int pokemonId, String nickname, String pokemonName, String pictureUrl, int height, int weight, List<String> types) {
}
