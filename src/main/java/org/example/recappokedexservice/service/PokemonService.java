package org.example.recappokedexservice.service;

import org.example.recappokedexservice.dto.PokemonResponse;
import org.example.recappokedexservice.exceptions.PokemonNotFoundException;
import org.example.recappokedexservice.model.Pokemon;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Service
public class PokemonService {

    private final RestClient restClient;

    public PokemonService(@Qualifier("pokemonRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public Pokemon getPokemonByName(String name) {
        PokemonResponse pokemonResponse;
        try {
            pokemonResponse = this.restClient.get()
                    .uri("/api/v2/pokemon/{name}", name)
                    .retrieve()
                    .body(PokemonResponse.class);
        } catch (RestClientException e) {
            throw new PokemonNotFoundException(name);
        }

        List<String> types = pokemonResponse.types().stream()
                .map(type -> type.type().name())
                .toList();

        return Pokemon.builder().build()
                .withPokemonId(pokemonResponse.id())
                .withPokemonName(pokemonResponse.name())
                .withHeight(pokemonResponse.height())
                .withWeight(pokemonResponse.weight())
                .withPictureUrl(pokemonResponse.sprites().other().official_artwork().front_default()).withTypes(types);
    }
}