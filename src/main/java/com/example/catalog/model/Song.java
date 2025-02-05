package com.example.catalog.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Song {
    private int durationMs;
    private String id;
    private String name;
    private int popularity;
    private String uri;
    private Album album;
    private List<Artist> artists;

    // Default constructor required for Jackson
    public Song() {
    }

    @JsonCreator
    public Song(
            @JsonProperty("duration_ms") int durationMs,
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("popularity") int popularity,
            @JsonProperty("uri") String uri,
            @JsonProperty("album") Album album,
            @JsonProperty("artists") List<Artist> artists) {
        this.durationMs = durationMs;
        this.id = id;
        this.name = name;
        this.popularity = popularity;
        this.uri = uri;
        this.album = album;
        this.artists = artists;
    }

    public Song(String id, String name) {
        this.id=id;
        this.name=name;
    }

    // Getters and setters
    public int getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }
}
