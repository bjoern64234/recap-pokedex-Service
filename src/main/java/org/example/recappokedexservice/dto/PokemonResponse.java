package org.example.recappokedexservice.dto;

import org.example.recappokedexservice.dto.artwork.SpritesDTO;
import org.example.recappokedexservice.dto.types.TypesDTO;

import java.util.List;

public record PokemonResponse(int id, String name, int height, int weight, SpritesDTO sprites, List<TypesDTO> types) {
}
