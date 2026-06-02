package org.example.recappokedexservice.service;

import org.example.recappokedexservice.dto.PokemonResponse;
import org.example.recappokedexservice.model.Pokemon;
import org.example.recappokedexservice.repository.PokemonRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class PokemonService {

    private final RestClient restClient;
    private final PokemonRepo pokemonRepo;
    private final IdService idService;

    public PokemonService(@Qualifier("pokemonRestClient") RestClient restClient, PokemonRepo pokemonRepo, IdService idService) {
        this.restClient = restClient;
        this.pokemonRepo = pokemonRepo;
        this.idService = idService;
    }

    public Pokemon getPokemonByName(String name) {
        PokemonResponse pokemonResponse = this.restClient.get().uri("/api/v2/pokemon/{name}", name).retrieve().body(PokemonResponse.class);

        List<String> types = pokemonResponse.types().stream()
                .map(type -> type.type().name())
                .toList();

        return Pokemon.builder().build()
                .withId(this.idService.generateId())
                .withPokemonId(pokemonResponse.id())
                .withPokemonName(pokemonResponse.name())
                .withHeight(pokemonResponse.height())
                .withWeight(pokemonResponse.weight())
                .withPictureUrl(pokemonResponse.sprites().other().official_artwork().front_default()).withTypes(types);
    }
}