package com.example.catalog.controller1;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import com.example.catalog.utils.SpotifyUtils;

@RestController
public class CatalogController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/popularSongs")
    public JsonNode getPopularSongs() throws IOException {
        ClassPathResource resource = new ClassPathResource("data/popular_songs.json");
        return objectMapper.readTree(resource.getFile());
    }

    @GetMapping("/popularArtists")
    public JsonNode getPopularArtists() throws IOException {
        ClassPathResource resource = new ClassPathResource("data/popular_artists.json");
        return objectMapper.readTree(resource.getFile());
    }

    @GetMapping("/albums/{id}")
    public JsonNode getAlbumById(@PathVariable String id) throws IOException {
        if (! SpotifyUtils.isValidId(id)) {
            return objectMapper.createObjectNode().put("error", "Invalid Id");
        }

        ClassPathResource resource = new ClassPathResource("data/albums.json");
        JsonNode albums = objectMapper.readTree(resource.getFile());
        JsonNode album = albums.get(id);
        if (album != null) {
            return album;
        } else {
            return objectMapper.createObjectNode().put("error", "Album not found");
        }
    }

}