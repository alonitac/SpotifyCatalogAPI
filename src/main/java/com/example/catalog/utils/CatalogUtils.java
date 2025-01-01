package com.example.catalog.utils;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CatalogUtils {

    /**
     * Sorts the list of songs by their name in alphabetical order.
     *
     * @param songs List of songs (JsonNode objects).
     * @return Sorted list of songs.
     */
    public List<JsonNode> sortSongsByName(List<JsonNode> songs) {
        songs.sort(Comparator.comparing(song -> song.get("name").asText()));
        return songs;
    }

    /**
     * Filters the list of songs by popularity.
     *
     * @param songs List of songs (JsonNode objects).
     * @param minPopularity The minimal popularity to filter
     * @return List of songs filtered by popularises great or equal to minPopularity.
     */
    public List<JsonNode> filterSongsByPopularity(List<JsonNode> songs, int minPopularity) {
        return songs.stream()
                .filter(song -> song.get("popularity").asInt() >= minPopularity)
                .collect(Collectors.toList());
    }

    /**
     * Checks if a song exists by its title.
     *
     * @param songs List of songs (JsonNode objects).
     * @param name The name of the song to search for (ignore case).
     * @return true if the song exists, false otherwise.
     */
    public boolean doesSongExistByName(List<JsonNode> songs, String name) {
        return songs.stream()
                .anyMatch(song -> song.get("name").asText().equalsIgnoreCase(name));
    }

    /**
     * Counts the number of songs belonging to a specific artist.
     *
     * @param songs List of songs (JsonNode objects).
     * @param artist The artist to count songs by.
     * @return The number of songs in the specified artist.
     */
    public long countSongsByArtist(List<JsonNode> songs, String artist) {

        return songs.stream()
                .filter(song -> StreamSupport.stream(song.get("artists").spliterator(), false)
                        .anyMatch(artistNode -> artistNode.get("name").asText().equalsIgnoreCase(artist)))
                .count();
    }

    /**
     * Returns the longest song.
     *
     * @param songs List of songs (JsonNode objects).
     * @return The longest song.
     */
    public static JsonNode getLongestSong(List<JsonNode> songs) {
        return songs.stream()
                .max(Comparator.comparingInt(song -> song.get("duration_ms").asInt()))
                .orElse(null);
    }

    /**
     * Finds a song released in the specified year.
     *
     * @param songs List of songs (JsonNode objects).
     * @param year  The release year to search for.
     * @return The first song released in the specified year.
     */
    public List<JsonNode> getSongByYear(List<JsonNode> songs, int year) {
        return songs.stream()
                .filter(song -> song.get("album").get("release_date").asText().substring(0, 4).equals(String.valueOf(year)))
                .toList();
    }

    /**
     * Returns the most recent song (latest release year).
     *
     * @param songs List of songs (JsonNode objects).
     * @return The most recent song.
     */
    public static JsonNode getMostRecentSong(List<JsonNode> songs) {
        return songs.stream()
                .max(Comparator.comparing(song -> song.get("album").get("release_date").asText()))
                .orElse(null);
    }
}
