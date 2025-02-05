package com.example.catalog.controller;

import com.example.catalog.model.Song;
import com.example.catalog.services.DataSourceService;
import com.example.catalog.utils.SpotifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {

    private final DataSourceService dataSourceService;

    @Autowired
    public SongController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    // GET /songs - List all songs
    @GetMapping//5
    public ResponseEntity<List<Song>> getAllSongs() {
        try {
            List<Song> songs = dataSourceService.getAllSongs();
            return ResponseEntity.ok(songs);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // GET /songs/{id} - Retrieve a song by its ID
    @GetMapping("/{id}")//1
    public ResponseEntity<Song> getSongById(@PathVariable String id) {
        try {
            Song song = dataSourceService.getSongById(id);
            if (song == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
            }
            return ResponseEntity.ok(song);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // POST /songs - Create a new song
    @PostMapping//2
    public ResponseEntity<Song> createSong(@RequestBody Song song) {
        try {
            if (song == null || song.getId() == null || song.getId().isEmpty()) {
                return ResponseEntity.badRequest().build(); // 400 Bad Request
            }
            Song createdSong = dataSourceService.createSong(song);
            return ResponseEntity.ok().body(createdSong); // 201 Created
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // PUT /songs/{id} - Update a song's details by its ID
    @PutMapping("/{id}")//3
    public ResponseEntity<Song> updateSong(@PathVariable String id, @RequestBody Song song) {
        try {
            if (song == null) {

                return ResponseEntity.badRequest().build(); // 400 Bad Request
            }

            // Fetch song by id
            Song songCheck = dataSourceService.getSongById(id);
            if (songCheck == null) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
            }

            // Update song
            dataSourceService.updateSong(id, song);


            return ResponseEntity.ok(song); // 200 OK
        } catch (IOException e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    // DELETE /songs/{id} - Delete a song by its ID
    @DeleteMapping("/{id}")//4
    public ResponseEntity<Void> deleteSong(@PathVariable String id) {
        try {
            boolean deleted = dataSourceService.deleteSong(id);
            if (deleted) {
                return ResponseEntity.ok().build(); // 204 No Content
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }
}
