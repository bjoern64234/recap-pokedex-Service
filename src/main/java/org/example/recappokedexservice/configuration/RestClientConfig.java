package org.example.recappokedexservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient pokemonRestClient(RestClient.Builder builder) {
        return builder.baseUrl("https://pokeapi.co").build();
    }
}