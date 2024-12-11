package com.example.catalog;

import com.example.catalog.utils.CatalogUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import java.util.ArrayList;
import java.util.List;

class CatalogUtilsTest {

    private CatalogUtils catalogUtils;
    private List<JsonNode> songs;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        catalogUtils = new CatalogUtils();
        objectMapper = new ObjectMapper();

        // Sample song data for testing. TODO - Add more songs
        String jsonData = """
                    [
                        {
                          "duration_ms": 200040,
                          "name": "Blinding Lights",
                          "popularity": 87,
                          "album": {
                            "name": "After Hours",
                            "release_date": "2020-03-20",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "The Weeknd"
                            }
                          ]
                        },
                        {
                          "duration_ms": 210000,
                          "name": "Shape of You",
                          "popularity": 95,
                          "album": {
                            "name": "Divide",
                            "release_date": "2017-03-03",
                            "total_tracks": 12
                          },
                          "artists": [
                            {
                              "name": "Ed Sheeran"
                            }
                          ]
                        },
                        {
                          "duration_ms": 180000,
                          "name": "Rolling in the Deep",
                          "popularity": 90,
                          "album": {
                            "name": "21",
                            "release_date": "2011-01-24",
                            "total_tracks": 11
                          },
                          "artists": [
                            {
                              "name": "Adele"
                            }
                          ]
                        }
                    ]
                """;

        songs = new ArrayList<>();
        objectMapper.readTree(jsonData).forEach(songs::add);
    }
    @Test
    void testSortSongsByName() {
        List<JsonNode> sortedSongs = catalogUtils.sortSongsByName(songs);
        assertEquals("Blinding Lights", sortedSongs.get(0).get("name").asText());
        assertEquals("Rolling in the Deep", sortedSongs.get(1).get("name").asText());
        assertEquals("Shape of You", sortedSongs.get(2).get("name").asText());
    }
    @Test
    void testFilterSongsByPopularity(){
        List<JsonNode> filteredSongs = catalogUtils.filterSongsByPopularity(songs, 90);
        assertEquals(2, filteredSongs.size());
        assertTrue(filteredSongs.stream().anyMatch(song -> song.get("name").asText().equals("Shape of You")));
        assertTrue(filteredSongs.stream().anyMatch(song -> song.get("name").asText().equals("Rolling in the Deep")));


    }
    @Test
    void testDoesSongExistByName() {
        assertTrue(catalogUtils.doesSongExistByName(songs, "Blinding Lights"));
        assertFalse(catalogUtils.doesSongExistByName(songs, "Non Existent Song"));
    }

    @Test
    void testCountSongsByArtist() {
        long count = catalogUtils.countSongsByArtist(songs, "The Weeknd");
        assertEquals(1, count);

        count = catalogUtils.countSongsByArtist(songs, "Ed Sheeran");
        assertEquals(1, count);

        count = catalogUtils.countSongsByArtist(songs, "Adele");
        assertEquals(1, count);
    }

    @Test
    void testGetLongestSong() {
        JsonNode longestSong = catalogUtils.getLongestSong(songs);
        assertEquals("Shape of You", longestSong.get("name").asText());
        assertEquals(210000, longestSong.get("duration_ms").asInt());
    }

    @Test
    void testGetSongByYear() {
        List<JsonNode> songs2017 = catalogUtils.getSongByYear(songs, 2017);
        assertEquals(1, songs2017.size());
        assertEquals("Shape of You", songs2017.get(0).get("name").asText());
    }

    @Test
    void testGetMostRecentSong() {
        JsonNode mostRecentSong = catalogUtils.getMostRecentSong(songs);
        assertEquals("Blinding Lights", mostRecentSong.get("name").asText());
        assertEquals("2020-03-20", mostRecentSong.get("album").get("release_date").asText());
    }
}


