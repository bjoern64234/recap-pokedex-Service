package org.example.recappokedexservice.service;

import org.example.recappokedexservice.dto.client.FavoriteDTO;
import org.example.recappokedexservice.model.Pokemon;
import org.example.recappokedexservice.repository.PokemonRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    @Mock
    private PokemonRepo mockPokemonRepo;

    @Mock
    private PokemonService mockPokemonService;

    @Mock
    private IdService mockIdService;

    @InjectMocks
    private CollectionService collectionService;

    @Test
    void saveFavorite_shouldReturnSavedPokemon_whenValidFavoriteDTOIsGiven() {
        // Given
        String name = "clefairy";
        String nickname = "nickname";
        String generatedId = "1";

        FavoriteDTO favoriteDTO = FavoriteDTO.builder()
                .build()
                .withPokemonName(name)
                .withNickname(nickname);

        Pokemon fetchedPokemon = Pokemon.builder().build()
                .withPokemonId(35)
                .withPokemonName(name)
                .withPictureUrl("frontUrl")
                .withHeight(6)
                .withWeight(75)
                .withTypes(List.of("fairy"));

        Pokemon expectedPokemon = fetchedPokemon
                .withId(generatedId)
                .withNickname(nickname);

        when(mockPokemonService.getPokemonByName(name)).thenReturn(fetchedPokemon);
        when(mockIdService.generateId()).thenReturn(generatedId);
        when(mockPokemonRepo.save(expectedPokemon)).thenReturn(expectedPokemon);

        // When
        Pokemon actual = collectionService.saveFavorite(favoriteDTO);

        // Then
        assertEquals(expectedPokemon, actual);
        verify(mockPokemonService).getPokemonByName(name);
        verify(mockIdService).generateId();
        verify(mockPokemonRepo).save(expectedPokemon);
    }
}