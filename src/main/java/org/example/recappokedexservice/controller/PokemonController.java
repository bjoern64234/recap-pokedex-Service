package org.example.recappokedexservice.controller;

import jakarta.validation.Valid;
import org.example.recappokedexservice.dto.client.FavoriteDTO;
import org.example.recappokedexservice.model.Pokemon;
import org.example.recappokedexservice.service.PokemonService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v2")
public class PokemonController {

    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/pokemon/{name}")
    public Pokemon getPokemonByName(@PathVariable @Valid String name) {
        return this.pokemonService.getPokemonByName(name);
    }

    @PostMapping("/collection")
    public FavoriteDTO getPokemonCollection(@RequestBody @Valid FavoriteDTO favoriteDTO) {
        return this.pokemonService.saveFavorite(favoriteDTO);
    }
}
