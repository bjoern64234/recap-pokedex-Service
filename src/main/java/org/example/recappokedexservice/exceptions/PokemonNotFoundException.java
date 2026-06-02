package org.example.recappokedexservice.exceptions;

public class PokemonNotFoundException extends RuntimeException {
    public PokemonNotFoundException(String name) {
        super("The pokemon with the name " + name + " was not found.");
    }
}
