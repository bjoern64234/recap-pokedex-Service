package org.example.recappokedexservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PokemonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Test
    void getPokemonByName_shouldReturnMappedPokemon_whenPokeApiResponds() throws Exception {
        // Given
        this.mockRestServiceServer
                .expect(requestTo("https://pokeapi.co/api/v2/pokemon/clefairy"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("""
                        {
                          "id": 35,
                          "name": "clefairy",
                          "height": 6,
                          "weight": 75,
                          "sprites": {
                            "other": {
                              "official-artwork": {
                                "front_default": "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/35.png"
                              }
                            }
                          },
                          "types": [
                            {
                              "slot": 1,
                              "type": {
                                "name": "fairy",
                                "url": "https://pokeapi.co/api/v2/type/18/"
                              }
                            }
                          ]
                        }
                        """, MediaType.APPLICATION_JSON));

        // When & Then
        this.mockMvc.perform(get("/api/v2/pokemon/clefairy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pokemonId").value(35))
                .andExpect(jsonPath("$.pokemonName").value("clefairy"))
                .andExpect(jsonPath("$.height").value(6))
                .andExpect(jsonPath("$.weight").value(75))
                .andExpect(jsonPath("$.pictureUrl").isNotEmpty())
                .andExpect(jsonPath("$.types[0]").value("fairy"))
                .andExpect(jsonPath("$.nickname").isEmpty());

        this.mockRestServiceServer.verify();
    }

    @Test
    void getPokemonByName_shouldReturn404_whenPokemonDoesNotExist() throws Exception {
        // Given
        this.mockRestServiceServer
                .expect(requestTo("https://pokeapi.co/api/v2/pokemon/notapokemon"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.NOT_FOUND));
        // When & Then
        this.mockMvc.perform(get("/api/v2/pokemon/notapokemon"))
                .andExpect(status().isNotFound());
        this.mockRestServiceServer.verify();
    }
}