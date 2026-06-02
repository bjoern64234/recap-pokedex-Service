package org.example.recappokedexservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.example.recappokedexservice.dto.client.FavoriteDTO;
import org.example.recappokedexservice.model.Pokemon;
import org.example.recappokedexservice.service.PokemonService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v2")
@Validated
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

    @GetMapping("/collection")
    public List<Pokemon> getPokemonCollection() {
        return this.pokemonService.getFavorites();
    }

    @GetMapping("/collection/{id}")
    public Pokemon getPokemonCollection(@PathVariable @Pattern(regexp = "[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}", message = "The requested id must be a type of uuid.") String id) {
        return this.pokemonService.getFavoriteById(id);
    }

    @DeleteMapping("/collection/{id}")
    public Map<String, String> deletePokemonCollection(@PathVariable @Pattern(regexp = "[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}", message = "The requested id must be a type of uuid.") String id) {
        this.pokemonService.deleteFavoriteById(id);

        return Map.of("message", "The pokemon with the id " + id + " was deleted.");
    }
}
