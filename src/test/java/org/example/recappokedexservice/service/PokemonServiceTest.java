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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

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

    @Test
    void saveFavorite_shouldThrowException_whenPokemonNameIsInvalid() {
        // Given
        FavoriteDTO favoriteDTO = FavoriteDTO.builder()
                .build()
                .withPokemonName("invalidname")
                .withNickname("nickname");

        when(mockPokemonService.getPokemonByName("invalidname"))
                .thenThrow(new PokemonNotFoundException("invalidname"));
        // When & Then
        assertThrows(RuntimeException.class, () -> collectionService.saveFavorite(favoriteDTO));
        verify(mockPokemonService).getPokemonByName("invalidname");
        verifyNoInteractions(mockIdService);
        verifyNoInteractions(mockPokemonRepo);
    }

    @Test
    void getFavoriteById_shouldThrowCollectionEntryNotFoundException_whenIdDoesNotExist() {
        // Given
        String id = "nonexistent-id";
        when(mockPokemonRepo.getPokemonById(id)).thenReturn(null);
        // When & Then
        assertThrows(CollectionEntryNotFoundException.class,
                () -> collectionService.getFavoriteById(id));
        verify(mockPokemonRepo).getPokemonById(id);
    }

    @Test
    void getFavoriteById_shouldThrowWithCorrectMessage_whenIdDoesNotExist() {
        // Given
        String id = "nonexistent-id";
        when(mockPokemonRepo.getPokemonById(id)).thenReturn(null);
        // When
        CollectionEntryNotFoundException ex = assertThrows(
                CollectionEntryNotFoundException.class,
                () -> collectionService.getFavoriteById(id)
        );
        // Then
        assertEquals("Pokemon was not found", ex.getMessage());
    }

    @Test
    void updateFavoriteById_shouldThrowCollectionEntryNotFoundException_whenIdDoesNotExist() {
        // Given
        String id = "nonexistent-id";
        FavoriteDTO favoriteDTO = FavoriteDTO.builder()
                .build()
                .withNickname("newnickname");

        when(mockPokemonRepo.getPokemonById(id)).thenReturn(null);
        // When & Then
        assertThrows(CollectionEntryNotFoundException.class,
                () -> collectionService.updateFavoriteById(id, favoriteDTO));
        verify(mockPokemonRepo).getPokemonById(id);
        verify(mockPokemonRepo, never()).save(any());
    }

    @Test
    void saveFavorite_shouldThrowException_whenRepoFails() {
        // Given
        String name = "clefairy";
        FavoriteDTO favoriteDTO = FavoriteDTO.builder()
                .build()
                .withPokemonName(name)
                .withNickname("nickname");

        Pokemon fetchedPokemon = Pokemon.builder().build()
                .withPokemonName(name);

        when(mockPokemonService.getPokemonByName(name)).thenReturn(fetchedPokemon);
        when(mockIdService.generateId()).thenReturn("1");
        when(mockPokemonRepo.save(any(Pokemon.class)))
                .thenThrow(new RuntimeException("Database error"));
        // When & Then
        assertThrows(RuntimeException.class, () -> collectionService.saveFavorite(favoriteDTO));
        verify(mockPokemonRepo).save(any(Pokemon.class));
    }

    @Test
    void getFavorites_shouldReturnEmptyList_whenCollectionIsEmpty() {
        // Given
        when(mockPokemonRepo.findAll()).thenReturn(List.of());
        // When
        List<Pokemon> actual = collectionService.getFavorites();
        // Then
        assertTrue(actual.isEmpty());
        verify(mockPokemonRepo).findAll();
    }

    @Test
    void deleteFavoriteById_shouldStillComplete_whenIdDoesNotExist() {
        // Given
        String id = "nonexistent-id";
        doNothing().when(mockPokemonRepo).deleteById(id);
        // When & Then
        assertDoesNotThrow(() -> collectionService.deleteFavoriteById(id));
        verify(mockPokemonRepo).deleteById(id);
    }
}