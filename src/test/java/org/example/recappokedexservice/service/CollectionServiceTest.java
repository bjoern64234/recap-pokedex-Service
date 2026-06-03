package org.example.recappokedexservice.service;

import org.example.recappokedexservice.dto.client.FavoriteDTO;
import org.example.recappokedexservice.exceptions.CollectionEntryNotFoundException;
import org.example.recappokedexservice.exceptions.PokemonNotFoundException;
import org.example.recappokedexservice.model.Pokemon;
import org.example.recappokedexservice.repository.PokemonRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollectionServiceTest {

    @Mock
    private PokemonRepo mockPokemonRepo;

    @Mock
    private PokemonService mockPokemonService;

    @Mock
    private IdService mockIdService;

    @InjectMocks
    private CollectionService collectionService;

    @Test
    void saveFavorite_shouldReturnSavedPokemon_whenValidDTOIsGiven() {
        // Given
        String name = "clefairy";
        String nickname = "myfairy";
        String id = "1";

        FavoriteDTO favoriteDTO = new FavoriteDTO(name, nickname);

        Pokemon pokemonFromApi = Pokemon.builder()
                .pokemonId(35)
                .pokemonName(name)
                .pictureUrl("frontUrl")
                .height(6)
                .weight(75)
                .types(List.of("fairy"))
                .build();

        Pokemon expected = pokemonFromApi
                .withId(id)
                .withNickname(nickname);

        when(mockPokemonService.getPokemonByName(name)).thenReturn(pokemonFromApi);
        when(mockIdService.generateId()).thenReturn(id);
        when(mockPokemonRepo.save(expected)).thenReturn(expected);
        // When
        Pokemon actual = collectionService.saveFavorite(favoriteDTO);
        // Then
        assertEquals(expected, actual);
        verify(mockPokemonService).getPokemonByName(name);
        verify(mockIdService).generateId();
        verify(mockPokemonRepo).save(expected);
    }

    @Test
    void saveFavorite_shouldThrowException_whenPokemonNameIsInvalid() {
        // Given
        String name = "unknown";
        String nickname = "myfairy";

        FavoriteDTO favoriteDTO = new FavoriteDTO(name, nickname);

        when(mockPokemonService.getPokemonByName(name))
                .thenThrow(new PokemonNotFoundException(name));

        // When & Then
        assertThrows(PokemonNotFoundException.class,
                () -> collectionService.saveFavorite(favoriteDTO));

        verify(mockPokemonService).getPokemonByName(name);
        verifyNoInteractions(mockIdService);
        verifyNoInteractions(mockPokemonRepo);
    }

    @Test
    void getFavorites_shouldReturnAllPokemons() {
        // Given
        Pokemon pokemon1 = Pokemon.builder()
                .pokemonId(35)
                .pokemonName("clefairy")
                .build();
        Pokemon pokemon2 = Pokemon.builder()
                .pokemonId(1)
                .pokemonName("bulbasaur")
                .build();

        when(mockPokemonRepo.findAll()).thenReturn(List.of(pokemon1, pokemon2));
        // When
        List<Pokemon> actual = collectionService.getFavorites();
        // Then
        assertEquals(List.of(pokemon1, pokemon2), actual);
        verify(mockPokemonRepo).findAll();
    }

    @Test
    void getFavoriteById_shouldReturnPokemon_whenIdExists() {
        // Given
        String id = "1";
        Pokemon expected = Pokemon.builder()
                .id(id)
                .pokemonId(35)
                .pokemonName("clefairy")
                .build();

        when(mockPokemonRepo.getPokemonById(id)).thenReturn(expected);
        // When
        Pokemon actual = collectionService.getFavoriteById(id);
        // Then
        assertEquals(expected, actual);
        verify(mockPokemonRepo).getPokemonById(id);
    }

    @Test
    void getFavoriteById_shouldThrowException_whenIdDoesNotExist() {
        // Given
        String id = "unknown";
        when(mockPokemonRepo.getPokemonById(id)).thenReturn(null);
        // When & Then
        assertThrows(CollectionEntryNotFoundException.class,
                () -> collectionService.getFavoriteById(id));
        verify(mockPokemonRepo).getPokemonById(id);
    }

    @Test
    void deleteFavoriteById_shouldDeletePokemon_whenIdExists() {
        // Given
        String id = "1";
        // When
        collectionService.deleteFavoriteById(id);
        // Then
        verify(mockPokemonRepo).deleteById(id);
    }

    @Test
    void updateFavoriteById_shouldUpdateNickname_whenIdExists() {
        // Given
        String id = "1";
        String newNickname = "newNick";
        FavoriteDTO favoriteDTO = new FavoriteDTO("clefairy", newNickname);

        Pokemon existing = Pokemon.builder()
                .id(id)
                .pokemonId(35)
                .pokemonName("clefairy")
                .nickname("oldNick")
                .build();

        Pokemon updated = existing.withNickname(newNickname);

        when(mockPokemonRepo.getPokemonById(id)).thenReturn(existing);
        // When
        collectionService.updateFavoriteById(id, favoriteDTO);
        // Then
        verify(mockPokemonRepo).getPokemonById(id);
        verify(mockPokemonRepo).save(updated);
    }
}