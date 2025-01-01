package com.example.catalog;


import com.example.catalog.utils.CatalogUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;



import static com.example.catalog.utils.SpotifyUtils.isValidId;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CatalogUtilsTest {

    private CatalogUtils catalogUtils;
    private List<JsonNode> songs;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        catalogUtils = new CatalogUtils();
        objectMapper = new ObjectMapper();

        String jsonData = """
                    [
                        {
                              "duration_ms": 209438,
                              "id": "2XU0oxnq2qxCpomAAuJY8K",
                              "name": "Dance Monkey",
                              "popularity": 72,
                              "uri": "spotify:track:2XU0oxnq2qxCpomAAuJY8K",
                              "album": {
                                "id": "0UywfDKYlyiu1b38DRrzYD",
                                "name": "Dance Monkey (Stripped Back) / Dance Monkey",
                                "uri": "spotify:album:0UywfDKYlyiu1b38DRrzYD",
                                "release_date": "2019-10-17",
                                "total_tracks": 2,
                                "images": [
                                  {
                                    "width": 640,
                                    "height": 640,
                                    "url": null
                                  },
                                  {
                                    "url": "ab67616d00001e02c6f7af36ecdc3ed6e0a1f169.jpeg",
                                    "width": 300,
                                    "height": 300
                                  },
                                  {
                                    "url": "ab67616d00004851c6f7af36ecdc3ed6e0a1f169.jpeg",
                                    "width": 64,
                                    "height": 64
                                  }
                                ]
                              },
                              "artists": [
                                {
                                  "id": "2NjfBq1NflQcKSeiDooVjY",
                                  "name": "Tones And I",
                                  "uri": "spotify:artist:2NjfBq1NflQcKSeiDooVjY"
                                }
                              ]
                            },
                          {
                            "duration_ms": 200040,
                            "id": "0VjIjW4GlUZAMYd2vXMi3b",
                            "name": "Blinding Lights",
                            "popularity": 87,
                            "uri": "spotify:track:0VjIjW4GlUZAMYd2vXMi3b",
                            "album": {
                              "id": "4yP0hdKOZPNshxUOjY0cZj",
                              "name": "After Hours",
                              "uri": "spotify:album:4yP0hdKOZPNshxUOjY0cZj",
                              "release_date": "2020-03-20",
                              "total_tracks": 14,
                              "images": [
                                {
                                  "width": 640,
                                  "height": 640,
                                  "url": null
                                },
                                {
                                  "url": "ab67616d00001e028863bc11d2aa12b54f5aeb36.jpeg",
                                  "width": 300,
                                  "height": 300
                                },
                                {
                                  "url": "ab67616d000048518863bc11d2aa12b54f5aeb36.jpeg",
                                  "width": 64,
                                  "height": 64
                                }
                              ]
                            },
                            "artists": [
                              {
                                "id": "1Xyo4u8uXC1ZmMpatF05PJ",
                                "name": "The Weeknd",
                                "uri": "spotify:artist:1Xyo4u8uXC1ZmMpatF05PJ"
                              }
                            ]
                          },
                          {
                            "duration_ms": 233712,
                            "id": "7qiZfU4dY1lWllzX7mPBI3",
                            "name": "Shape of You",
                            "popularity": 86,
                            "uri": "spotify:track:7qiZfU4dY1lWllzX7mPBI3",
                            "album": {
                              "id": "3T4tUhGYeRNVUGevb0wThu",
                              "name": "\\u00f7 (Deluxe)",
                              "uri": "spotify:album:3T4tUhGYeRNVUGevb0wThu",
                              "release_date": "2017-03-03",
                              "total_tracks": 16,
                              "images": [
                                {
                                  "width": 640,
                                  "height": 640,
                                  "url": null
                                },
                                {
                                  "url": "ab67616d00001e02ba5db46f4b838ef6027e6f96.jpeg",
                                  "width": 300,
                                  "height": 300
                                },
                                {
                                  "url": "ab67616d00004851ba5db46f4b838ef6027e6f96.jpeg",
                                  "width": 64,
                                  "height": 64
                                }
                              ]
                            },
                            "artists": [
                              {
                                "id": "6eUKZXaKkcviH0Ku9w2n3V",
                                "name": "Ed Sheeran",
                                "uri": "spotify:artist:6eUKZXaKkcviH0Ku9w2n3V"
                              }
                            ]
                          }
                    ]
                """;

        songs = new ArrayList<>();
        objectMapper.readTree(jsonData).forEach(songs::add);
    }


    @Test
    public void testsortSongsByName() {
        List<JsonNode> sortedSongs = catalogUtils.sortSongsByName(songs);
        assertEquals("Blinding Lights", sortedSongs.get(0).get("name").asText());
        assertEquals("Dance Monkey", sortedSongs.get(1).get("name").asText());
        assertEquals("Shape of You", sortedSongs.get(2).get("name").asText());

    }


    @Test
    public void testfilterSongsByPopularity() {
        List<JsonNode> filteredSongs = catalogUtils.filterSongsByPopularity(songs,80);
        boolean existsDanceMonkey  = filteredSongs.stream().anyMatch(song -> "Dance Monkey".equals(song.get("name").asText()));
        assertFalse(existsDanceMonkey);
        boolean existsBlindingLights  = filteredSongs.stream().anyMatch(song -> "Blinding Lights".equals(song.get("name").asText()));
        assertTrue(existsBlindingLights);
        boolean existsShapeofYou  = filteredSongs.stream().anyMatch(song -> "Shape of You".equals(song.get("name").asText()));
        assertTrue(existsShapeofYou);
    }


    @Test
    public void testdoesSongExistByName() {

        assertTrue(songs.stream().anyMatch(song -> "Dance Monkey".equalsIgnoreCase(song.get("name").asText())));
        assertTrue(songs.stream().anyMatch(song -> "Blinding Lights".equalsIgnoreCase(song.get("name").asText())));

        assertTrue(songs.stream().anyMatch(song -> "Shape of You".equalsIgnoreCase(song.get("name").asText())));
        assertTrue(songs.stream().anyMatch(song -> "shape of you".equalsIgnoreCase(song.get("name").asText())));

        assertFalse(songs.stream().anyMatch(song -> "asddgfdsgdsf".equalsIgnoreCase(song.get("name").asText())));


    }

    @Test
    public void testcountSongsByArtist() {
        assertEquals(0,catalogUtils.countSongsByArtist(songs,"John"));
        assertEquals(1,catalogUtils.countSongsByArtist(songs,"The Weeknd"));

    }

    @Test
    public void testgetLongestSong() {
        assertEquals("Shape of You",catalogUtils.getLongestSong(songs).get("name").asText());
    }

    @Test
    public void testgetSongByYear() {
        assertEquals(1,catalogUtils.getSongByYear(songs,2020).stream().count());
        assertEquals(0,catalogUtils.getSongByYear(songs,2024).stream().count());
    }

    @Test
    public void testgetMostRecentSong() {
        assertEquals("Blinding Lights",catalogUtils.getMostRecentSong(songs).get("name").asText());
    }




}