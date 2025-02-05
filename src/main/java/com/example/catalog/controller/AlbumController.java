package com.example.catalog.controller;

import com.example.catalog.model.Album;
import com.example.catalog.model.Track;
import com.example.catalog.services.DataSourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    private final DataSourceService dataSourceService;

    public AlbumController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @GetMapping//1
    public ResponseEntity<List<Album>> getAllAlbums() {
        try {
            return ResponseEntity.ok(dataSourceService.getAllAlbums());
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);  // Internal Server Error
        }
    }

    @GetMapping("/{id}")//2
    public ResponseEntity<Album> getAlbumById(@PathVariable String id) {
        try {
            Album album = dataSourceService.getAlbumById(id);
            if (album == null) {
                return ResponseEntity.status(404).body(null);  // Not Found
            }
            return ResponseEntity.ok(album);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);  // Internal Server Error
        }
    }

    @PostMapping//3
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) {
        if (album == null || album.getId() == null) {
            return ResponseEntity.status(400).body(null);  // Bad Request
        }
        try {
            return ResponseEntity.ok(dataSourceService.createAlbum(album));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);  // Internal Server Error
        }
    }

    @PutMapping("/{id}")//4
    public ResponseEntity<Album> updateAlbum(@PathVariable String id, @RequestBody Album album) {
        if (id == null || album == null) {
            return ResponseEntity.status(400).body(null);  // Bad Request
        }
        try {
            Album updatedAlbum = dataSourceService.updateAlbum(id, album);
            if (updatedAlbum == null) {
                return ResponseEntity.status(404).body(null);  // Not Found
            }
            return ResponseEntity.ok(updatedAlbum);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);  // Internal Server Error
        }
    }

    @DeleteMapping("/{id}")//5
    public ResponseEntity<Void> deleteAlbum(@PathVariable String id) {
        try {
            boolean isDeleted = dataSourceService.deleteAlbum(id);
            if (!isDeleted) {
                return ResponseEntity.status(404).build();  // Not Found
            }
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).build();  // Internal Server Error
        }
    }

    @GetMapping("/{id}/tracks")//6
    public ResponseEntity<List<Track>> getAlbumTracks(@PathVariable String id) {
        try {
            List<Track> tracks = dataSourceService.getTracks(id);
            if (tracks == null || tracks.isEmpty()) {
                return ResponseEntity.status(404).body(null);  // Not Found
            }
            return ResponseEntity.ok(tracks);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);  // Internal Server Error
        }
    }

    @PostMapping("/{id}/tracks")//7
    public ResponseEntity<Album> addTrackToAlbum(@PathVariable String id, @RequestBody Track track) throws IOException {
        if (track == null || id == null) {
            return ResponseEntity.status(400).body(null);  // Bad Request
        }

        // Check if the album with the given ID exists
        Album album = dataSourceService.getAlbumById(id);
        if (album == null) {
            return ResponseEntity.status(404).body(null);  // Not Found
        }

        try {
            // Add the track to the album
            Album updatedAlbum = dataSourceService.addTrack(id, track);
            return ResponseEntity.ok(updatedAlbum);  // OK with the updated album
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);  // Internal Server Error
        }
    }

    @PutMapping("/{id}/tracks/{track_id}")//8
    public ResponseEntity<Track> updateTrack(@PathVariable String id, @PathVariable String track_id, @RequestBody Track track) {
        if (id == null || track_id == null || track == null) {
            return ResponseEntity.status(400).body(null);  // Bad Request
        }
        try {
            Track updatedTrack = dataSourceService.updateTrack(id, track_id, track);
            if (updatedTrack == null) {
                return ResponseEntity.status(404).body(null);  // Not Found
            }
            return ResponseEntity.ok(updatedTrack);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);  // Internal Server Error
        }
    }

    @DeleteMapping("/{id}/tracks/{track_id}")//9
    public ResponseEntity<Void> deleteTrack(@PathVariable String id, @PathVariable String track_id) {
        try {
            boolean isDeleted = dataSourceService.deleteTrack(id, track_id);
            if (!isDeleted) {
                return ResponseEntity.status(404).build();  // Not Found
            }
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).build();  // Internal Server Error
        }
    }
}
