package org.example.recappokedexservice.repository;

import org.example.recappokedexservice.model.Pokemon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PokemonRepo extends MongoRepository<Pokemon, String> {
    Pokemon getPokemonById(String id);
}
