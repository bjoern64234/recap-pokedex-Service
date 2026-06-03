package org.example.recappokedexservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.example.recappokedexservice.dto.client.FavoriteDTO;
import org.example.recappokedexservice.model.Pokemon;
import org.example.recappokedexservice.service.CollectionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Validated
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @PostMapping("/collection")
    public Pokemon getPokemonCollection(@RequestBody @Valid FavoriteDTO favoriteDTO) {
        return this.collectionService.saveFavorite(favoriteDTO);
    }

    @GetMapping("/collection")
    public List<Pokemon> getPokemonCollection() {
        return this.collectionService.getFavorites();
    }

    @GetMapping("/collection/{id}")
    public Pokemon getPokemonCollection(@PathVariable @Pattern(regexp = "[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}", message = "The requested id must be a type of uuid.") String id) {
        return this.collectionService.getFavoriteById(id);
    }

    @DeleteMapping("/collection/{id}")
    public Map<String, String> deletePokemonCollection(@PathVariable @Pattern(regexp = "[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}", message = "The requested id must be a type of uuid.") String id) {
        this.collectionService.deleteFavoriteById(id);

        return Map.of("message", "The pokemon with the id " + id + " was deleted.");
    }

    @PutMapping("/collection/{id}")
    public Map<String, String> updatePokemonCollection(@RequestBody @Valid FavoriteDTO favoriteDTO, @PathVariable @Pattern(regexp = "[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}") String id) {
        this.collectionService.updateFavoriteById(id, favoriteDTO);

        return Map.of("nickname", favoriteDTO.nickname());
    }
}
