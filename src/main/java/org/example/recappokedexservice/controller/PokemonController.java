package org.example.recappokedexservice.controller;

import org.example.recappokedexservice.dto.PokemonResponse;
import org.example.recappokedexservice.model.Pokemon;
import org.example.recappokedexservice.service.PokemonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2")
public class PokemonController {

    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping("/pokemon/{name}")
    public Pokemon getPokemonByName(@PathVariable String name) {
        return this.pokemonService.getPokemonByName(name);
    }
}
