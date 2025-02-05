package com.example.catalog.controller;

import com.example.catalog.model.Artist;
import com.example.catalog.services.DataSourceService;
import com.example.catalog.utils.SpotifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {

    private final DataSourceService dataSourceService;

    @Autowired
    public ArtistController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @GetMapping("/{id}") // 1
    public ResponseEntity<Artist> getArtistById(@PathVariable String id) throws IOException {

            Artist artist = dataSourceService.getArtistById(id);
            if (artist == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
            }


        return ResponseEntity.ok(artist);
        }



    @GetMapping // 2
    public ResponseEntity<List<Artist>> getAllArtists() {
        try {
            List<Artist> artists = dataSourceService.getAllArtists();
            return ResponseEntity.ok(artists); // 200 OK
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @PostMapping // 3
    public ResponseEntity<Artist> addArtist(@RequestBody Artist artist) {
        try {
            if (artist == null || artist.getName() == null || artist.getName().isEmpty()) {
                return ResponseEntity.badRequest().build(); // 400 Bad Request
            }

            // Assuming the addArtist method returns the saved artist
            Artist createdArtist = dataSourceService.addArtist(artist);
            return ResponseEntity.ok().body(createdArtist); // 201 Created with the artist in the response body
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @PutMapping("/{id}") // 4
    public ResponseEntity<Artist> updateArtist(@PathVariable String id, @RequestBody Artist artist) {

        try {
            if (artist == null) {
                return ResponseEntity.badRequest().build(); // 400 Bad Request
            }

            Artist existingArtist = dataSourceService.getArtistById(id);
            if (existingArtist == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
            }

            Artist updated = dataSourceService.updateArtist(id,artist);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
            }

            return ResponseEntity.ok().body(updated); // 200 OK
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/{id}") // 5
    public ResponseEntity<Void> deleteArtist(@PathVariable String id) throws IOException {


            boolean deleted = dataSourceService.deleteArtist(id);
            if (deleted) {
                return ResponseEntity.ok().build(); // 200 id deleted
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found

    }
}
