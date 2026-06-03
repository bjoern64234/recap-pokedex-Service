package org.example.recappokedexservice.service;

import org.example.recappokedexservice.dto.client.FavoriteDTO;
import org.example.recappokedexservice.model.Pokemon;
import org.example.recappokedexservice.repository.PokemonRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PokemonServiceTest {

    private final PokemonRepo mockPokemonRepo = Mockito.mock(PokemonRepo.class);
    private final CollectionService mockCollectionService =  Mockito.mock(CollectionService.class);

    @Test
    void getPokemonByName_shouldReturnPokemon_whenValidNameIsGiven() {
        // Given
        String name = "clefairy";
        String nickname = "nickname";
        String id = "1";

        FavoriteDTO favoriteDTO = FavoriteDTO.builder().build().withPokemonName(name).withNickname(nickname);

        Pokemon expected = Pokemon.builder().build()
                .withId(id)
                .withPokemonId(35)
                .withPokemonName(name)
                .withNickname(nickname)
                .withPictureUrl("frontUrl")
                .withHeight(6)
                .withWeight(75)
                .withTypes(List.of("fairy"));

        when(this.mockPokemonRepo.save(expected)).thenReturn(expected);
        when(this.mockCollectionService.saveFavorite(favoriteDTO)).thenReturn(expected);
        // When
        Pokemon actual = this.mockCollectionService.saveFavorite(favoriteDTO);
        // Then
        assertEquals(expected, actual);
        verify(this.mockCollectionService).saveFavorite(favoriteDTO);
    }
}