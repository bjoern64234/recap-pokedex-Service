package org.example.recappokedexservice.service;

import org.example.recappokedexservice.dto.PokemonResponse;
import org.example.recappokedexservice.dto.artwork.OfficialArtworkDTO;
import org.example.recappokedexservice.dto.artwork.OtherDTO;
import org.example.recappokedexservice.dto.artwork.SpritesDTO;
import org.example.recappokedexservice.dto.types.TypeDTO;
import org.example.recappokedexservice.dto.types.TypesDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollectionServiceTest {

    @Test
    void saveFavorite() {
        // Given
        String name = "clefairy";
        String id = "1";

        TypeDTO typeDTO = new TypeDTO("fairy", "typeUrl");
        TypesDTO typesDTO = new TypesDTO(2, typeDTO);
        List<TypesDTO> typesDTOList = List.of(typesDTO);

        OfficialArtworkDTO officialArtworkDTO = new OfficialArtworkDTO("frontUrl", "backUrl");
        OtherDTO otherDTO = new OtherDTO(officialArtworkDTO);
        SpritesDTO spritesDTO = new SpritesDTO(otherDTO);

        PokemonResponse pokemonResponse = new PokemonResponse(35, name, 6, 75, spritesDTO, typesDTOList);
    }

    @Test
    void getFavorites() {
    }

    @Test
    void getFavoriteById() {
    }

    @Test
    void deleteFavoriteById() {
    }

    @Test
    void updateFavoriteById() {
    }
}