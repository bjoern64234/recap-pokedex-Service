package org.example.recappokedexservice.controller;

import org.example.recappokedexservice.model.Pokemon;
import org.example.recappokedexservice.repository.PokemonRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CollectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PokemonRepo pokemonRepo;

    // valid UUID for seeding
    private static final String VALID_ID = "550e8400-e29b-41d4-a716-446655440000";

    // -------------------------------------------------------------------------
    // POST /api/collection
    // -------------------------------------------------------------------------

    @Test
    void saveFavoritePokemon_shouldReturnPokemon_whenValidDTOIsGiven() throws Exception {
        mockMvc.perform(post("/api/collection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "pokemonName": "clefairy",
                                  "nickname": "myfairy"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.pokemonName").value("clefairy"))
                .andExpect(jsonPath("$.nickname").value("myfairy"));
    }

    // -------------------------------------------------------------------------
    // GET /api/collection
    // -------------------------------------------------------------------------

    @Test
    void getAllPokemon_shouldReturnEmptyList_whenCollectionIsEmpty() throws Exception {
        mockMvc.perform(get("/api/collection"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAllPokemon_shouldReturnList_whenCollectionHasEntries() throws Exception {
        // Given
        pokemonRepo.save(Pokemon.builder().build()
                .withId(VALID_ID)
                .withPokemonId(35)
                .withPokemonName("clefairy")
                .withNickname("myfairy")
                .withTypes(List.of("fairy")));

        // When & Then
        mockMvc.perform(get("/api/collection"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].pokemonName").value("clefairy"))
                .andExpect(jsonPath("$[0].nickname").value("myfairy"));
    }

    // -------------------------------------------------------------------------
    // GET /api/collection/{id}
    // -------------------------------------------------------------------------

    @Test
    void getPokemonById_shouldReturnPokemon_whenIdExists() throws Exception {
        // Given
        pokemonRepo.save(Pokemon.builder().build()
                .withId(VALID_ID)
                .withPokemonId(35)
                .withPokemonName("clefairy")
                .withNickname("myfairy")
                .withTypes(List.of("fairy")));

        // When & Then
        mockMvc.perform(get("/api/collection/" + VALID_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VALID_ID))
                .andExpect(jsonPath("$.pokemonName").value("clefairy"))
                .andExpect(jsonPath("$.nickname").value("myfairy"));
    }

    @Test
    void getPokemonById_shouldReturn404_whenIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/collection/" + VALID_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPokemonById_shouldReturn400_whenIdIsNotUUID() throws Exception {
        mockMvc.perform(get("/api/collection/not-a-uuid"))
                .andExpect(status().isBadRequest());
    }

    // -------------------------------------------------------------------------
    // DELETE /api/collection/{id}
    // -------------------------------------------------------------------------

    @Test
    void deletePokemonById_shouldReturnMessage_whenIdExists() throws Exception {
        // Given
        pokemonRepo.save(Pokemon.builder().build()
                .withId(VALID_ID)
                .withPokemonName("clefairy")
                .withNickname("myfairy"));

        // When & Then
        mockMvc.perform(delete("/api/collection/" + VALID_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("The pokemon with the id " + VALID_ID + " was deleted."));

        // Verify it's gone
        mockMvc.perform(get("/api/collection/" + VALID_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePokemonById_shouldReturn400_whenIdIsNotUUID() throws Exception {
        mockMvc.perform(delete("/api/collection/not-a-uuid"))
                .andExpect(status().isBadRequest());
    }

    // -------------------------------------------------------------------------
    // PUT /api/collection/{id}
    // -------------------------------------------------------------------------

    @Test
    void updatePokemonById_shouldReturnUpdatedNickname_whenIdExists() throws Exception {
        // Given
        pokemonRepo.save(Pokemon.builder().build()
                .withId(VALID_ID)
                .withPokemonId(35)
                .withPokemonName("clefairy")
                .withNickname("oldnickname")
                .withTypes(List.of("fairy")));

        // When & Then
        mockMvc.perform(put("/api/collection/" + VALID_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "pokemonName": "clefairy",
                                  "nickname": "newnickname"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("newnickname"));

        // Verify persisted
        mockMvc.perform(get("/api/collection/" + VALID_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").value("newnickname"));
    }

    @Test
    void updatePokemonById_shouldReturn404_whenIdDoesNotExist() throws Exception {
        mockMvc.perform(put("/api/collection/" + VALID_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "pokemonName": "clefairy",
                                  "nickname": "newnickname"
                                }
                                """))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePokemonById_shouldReturn400_whenIdIsNotUUID() throws Exception {
        mockMvc.perform(put("/api/collection/not-a-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "pokemonName": "clefairy",
                                  "nickname": "newnickname"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }
}