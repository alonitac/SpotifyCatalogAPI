package com.example.catalog.services;

import com.example.catalog.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
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
        String url = "https://api.spotify.com/v1/artists/" + id;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        ResponseEntity<JsonNode> response = restTemplate.getForEntity(builder.toUriString(),
                JsonNode.class, createHeaders());

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            JsonNode artistNode = response.getBody();
            return parseArtist(artistNode);
        } else {
            throw new IOException("Failed to fetch artist with ID: " + id);
        }
    }

    @Override
    public List<Artist> getAllArtists() throws IOException {
        String url = "https://api.spotify.com/v1/artists";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        ResponseEntity<JsonNode> response = restTemplate.getForEntity(builder.toUriString(),
                JsonNode.class, createHeaders());

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            List<Artist> artists = new ArrayList<>();
            for (JsonNode artistNode : response.getBody()) {
                artists.add(parseArtist(artistNode));
            }
            return artists;
        } else {
            throw new IOException("Failed to fetch artists.");
        }
    }

    // For methods that Spotify API does not support (add/update/delete), we throw 404 or 400 errors.
    @Override
    public Artist addArtist(Artist artist) throws IOException {
        // Spotify doesn't allow adding artists via the API.
        throw new IOException("Cannot add artist via Spotify API. This operation is not supported (404 Not Found).");


    }

    @Override
    public Artist updateArtist(String id, Artist artist) throws IOException {
        // Spotify doesn't allow updating artists via the API.
        throw new IOException("Cannot update artist via Spotify API. This operation is not supported (404 Not Found).");

    }

    @Override
    public boolean deleteArtist(String id) throws IOException {
        // Spotify doesn't allow deleting artists via the API.
        throw new IOException("Cannot delete artist via Spotify API. This operation is not supported (404 Not Found).");
    }

    @Override
    public List<Album> getAllAlbums() throws IOException {
        return List.of();
    }

    @Override
    public Album getAlbumById(String id) throws IOException {
        return null;
    }

    @Override
    public Album createAlbum(Album album) throws IOException {
        return null;
    }

    @Override
    public Album updateAlbum(String id, Album updatedAlbum) throws IOException {
        return null;
    }

    @Override
    public boolean deleteAlbum(String id) throws IOException {
        return false;
    }

    @Override
    public List<Track> getTracks(String albumId) throws IOException {
        return List.of();
    }

    @Override
    public Album addTrack(String albumId, Track track) throws IOException {
        return null;
    }

    @Override
    public Track updateTrack(String albumId, String trackId, Track updatedTrack) throws IOException {
        return null;
    }

    @Override
    public boolean deleteTrack(String albumId, String trackId) throws IOException {
        return false;
    }

    @Override
    public List<Song> getAllSongs() throws IOException {
        return List.of();
    }

    @Override
    public Song getSongById(String id) throws IOException {
        return null;
    }

    @Override
    public Song createSong(Song song) throws IOException {
        return null;
    }

    @Override
    public void updateSong(String id, Song song) throws IOException {
      return ;
    }

    @Override
    public boolean deleteSong(String id) throws IOException {
        return false;
    }

    private Artist parseArtist(JsonNode artistNode) {
        Artist artist = new Artist();
        artist.setId(artistNode.get("id").asText());
        artist.setName(artistNode.get("name").asText());
        artist.setFollowers(artistNode.get("followers").get("total").asInt());
        artist.setPopularity(artistNode.get("popularity").asInt());
        artist.setUri(artistNode.get("uri").asText());

        List<String> genres = new ArrayList<>();
        artistNode.get("genres").forEach(genreNode -> genres.add(genreNode.asText()));
        artist.setGenres(genres);

        List<Image> images = new ArrayList<>();
        artistNode.get("images").forEach(imageNode -> {
            Image image = new Image();
            image.setUrl(imageNode.get("url").asText());
            images.add(image);
        });
        artist.setImages(images);

        return artist;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + spotifyApiToken);
        return headers;
    }
}
