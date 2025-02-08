package com.example.catalog.services;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
public class SpotifyAPIDataSources implements DataSourceService {

    @Value("${spotify.api.token}") // Spotify API token
    private String spotifyApiToken;

    private final RestTemplate restTemplate;

    public SpotifyAPIDataSources(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    


    @Override
    public Artist getArtistById(String id) throws IOException {
        try {
            ResponseEntity<Artist> response = restTemplate.exchange(
                    "https://api.spotify.com/v1/artists/" + id, HttpMethod.GET, new HttpEntity<>(createHeaders()), Artist.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new IOException("Artist not found (Status Code: " + response.getStatusCode() + ")");
            }
        } catch (Exception e) {
            throw new IOException("Error fetching artist: " + e.getMessage());
        }
    }

    @Override
    public List<Artist> getAllArtists() throws IOException {
        try {
            ResponseEntity<Artist[]> response = restTemplate.exchange(
                    "https://api.spotify.com/v1/artists", HttpMethod.GET, new HttpEntity<>(createHeaders()), Artist[].class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return List.of(response.getBody());
            } else {
                throw new IOException("Error fetching artists (Status Code: " + response.getStatusCode() + ")");
            }
        } catch (Exception e) {
            throw new IOException("Error fetching all artists: " + e.getMessage());
        }
    }



    @Override
    public Artist addArtist(Artist artist)throws IOException   {
         return null;
    }

    @Override
    public Artist updateArtist(String id, Artist artist) throws IOException {

        return null;
    }

    @Override
    public boolean deleteArtist(String id) throws IOException {
        return false;
    }

    @Override
    public List<Album> getAllAlbums() throws IOException {
        try {
            ResponseEntity<Album[]> response = restTemplate.exchange(
                    "https://api.spotify.com/v1/me/albums", HttpMethod.GET, new HttpEntity<>(createHeaders()), Album[].class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return List.of(response.getBody());
            } else {
                throw new IOException("Error fetching albums (Status Code: " + response.getStatusCode() + ")");
            }
        } catch (Exception e) {
            throw new IOException("Error fetching all albums: " + e.getMessage());
        }
    }

    @Override
    public Album getAlbumById(String id) throws IOException {
        try {
            ResponseEntity<Album> response = restTemplate.exchange(
                    "https://api.spotify.com/v1/albums/" + id, HttpMethod.GET, new HttpEntity<>(createHeaders()), Album.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new IOException("Album not found (Status Code: " + response.getStatusCode() + ")");
            }
        } catch (Exception e) {
            throw new IOException("Error fetching album: " + e.getMessage());
        }
    }



    @Override
    public Album createAlbum(Album album) throws IOException {
        throw new IOException("Cannot create album via Spotify API. This operation is not supported.");
    }

    @Override
    public Album updateAlbum(String id, Album updatedAlbum) throws IOException {
        throw new IOException("Cannot update album via Spotify API. This operation is not supported.");
    }

    @Override
    public boolean deleteAlbum(String id) throws IOException {
        throw new IOException("Cannot delete album via Spotify API. This operation is not supported.");
    }

    @Override
    public List<Track> getTracks(String albumId) throws IOException {
        try {
            ResponseEntity<Track[]> response = restTemplate.exchange(
                    "https://api.spotify.com/v1/albums/" + albumId + "/tracks", HttpMethod.GET, new HttpEntity<>(createHeaders()), Track[].class);
            if (response.getStatusCode().is2xxSuccessful()) {
                return List.of(response.getBody());
            } else {
                throw new IOException("Error fetching tracks (Status Code: " + response.getStatusCode() + ")");
            }
        } catch (Exception e) {
            throw new IOException("Error fetching tracks for album: " + e.getMessage());
        }
    }


    @Override
    public Album addTrack(String albumId, Track track) throws IOException {
        throw new IOException("Cannot add track to album via Spotify API. This operation is not supported.");
    }

    @Override
    public Track updateTrack(String albumId, String trackId, Track updatedTrack) throws IOException {
        throw new IOException("Cannot update track via Spotify API. This operation is not supported.");
    }

    @Override
    public boolean deleteTrack(String albumId, String trackId) throws IOException {
        throw new IOException("Cannot delete track via Spotify API. This operation is not supported.");
    }

    @Override
    public List<Song> getAllSongs() throws IOException {
        throw new IOException("Cannot fetch all songs via Spotify API. This operation is not supported.");
    }

    @Override
    public Song getSongById(String id) throws IOException {
        throw new IOException("Cannot fetch song by ID via Spotify API. This operation is not supported.");
    }



    @Override
    public Song createSong(Song song) throws IOException {
        throw new IOException("Cannot create song via Spotify API. This operation is not supported.");
    }

    @Override
    public void updateSong(String id, Song song) throws IOException {
        throw new IOException("Cannot update song via Spotify API. This operation is not supported.");
    }

    @Override
    public boolean deleteSong(String id) throws IOException {
        throw new IOException("Cannot delete song via Spotify API. This operation is not supported.");
    }

    // Helper method to create headers with the Bearer token
    private HttpEntity<Object> createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + spotifyApiToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
        
       
        
    }
}
