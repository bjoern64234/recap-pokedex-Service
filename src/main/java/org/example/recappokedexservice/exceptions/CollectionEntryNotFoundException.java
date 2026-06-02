package org.example.recappokedexservice.exceptions;

public class CollectionEntryNotFoundException extends RuntimeException {
    public CollectionEntryNotFoundException(String message) {
        super(message);
    }
}
