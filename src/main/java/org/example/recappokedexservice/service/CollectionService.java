package org.example.recappokedexservice.service;

import org.example.recappokedexservice.dto.client.FavoriteDTO;
import org.example.recappokedexservice.exceptions.CollectionEntryNotFoundException;
import org.example.recappokedexservice.model.Pokemon;
import org.example.recappokedexservice.repository.PokemonRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionService {

    private final PokemonRepo pokemonRepo;
    private final PokemonService pokemonService;
    private final IdService idService;

    public CollectionService(PokemonRepo pokemonRepo, PokemonService pokemonService, IdService idService) {
        this.pokemonRepo = pokemonRepo;
        this.pokemonService = pokemonService;
        this.idService = idService;
    }

    public Pokemon saveFavorite(FavoriteDTO favoriteDTO) {
        Pokemon pokemon = this.pokemonService.getPokemonByName(favoriteDTO.pokemonName())
                .withId(this.idService.generateId())
                .withNickname(favoriteDTO.nickname());

        return this.pokemonRepo.save(pokemon);
    }

    public List<Pokemon> getFavorites() {
        return this.pokemonRepo.findAll();
    }

    public Pokemon getFavoriteById(String id) {
        Pokemon pokemon = this.pokemonRepo.getPokemonById(id);

        if (pokemon == null) {
            throw new CollectionEntryNotFoundException("Pokemon was not found");
        }

        return pokemon;
    }

    public void deleteFavoriteById(String id) {
        this.pokemonRepo.deleteById(id);
    }

    public void updateFavoriteById(String id, FavoriteDTO favoriteDTO) {
        Pokemon pokemon = this.getFavoriteById(id).withNickname(favoriteDTO.nickname());
        this.pokemonRepo.save(pokemon);
    }
}
