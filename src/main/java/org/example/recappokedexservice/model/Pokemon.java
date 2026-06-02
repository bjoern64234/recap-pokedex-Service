package org.example.recappokedexservice.model;

import lombok.Builder;
import lombok.With;
import org.example.recappokedexservice.dto.types.TypesDTO;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@With
@Builder
@Document("Pokemon")
public record Pokemon(String id, int pokemonId, String nickname, String pokemonName, String pictureUrl, int height, int weight, List<String> types) {
}
