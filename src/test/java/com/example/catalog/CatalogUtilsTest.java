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
                          "duration_ms": 215000,
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
                        },
                        {
                          "duration_ms": 190000,
                          "name": "Levitating",
                          "popularity": 88,
                          "album": {
                            "name": "Future Nostalgia",
                            "release_date": "2020-03-27",
                            "total_tracks": 11
                          },
                          "artists": [
                            {
                              "name": "Dua Lipa"
                            }
                          ]
                        },
                        {
                          "duration_ms": 210000,
                          "name": "Uptown Funk",
                          "popularity": 92,
                          "album": {
                            "name": "Uptown Special",
                            "release_date": "2014-11-10",
                            "total_tracks": 9
                          },
                          "artists": [
                            {
                              "name": "Mark Ronson",
                              "featuring": "Bruno Mars"
                            }
                          ]
                        },
                        {
                          "duration_ms": 220000,
                          "name": "Blowin' in the Wind",
                          "popularity": 85,
                          "album": {
                            "name": "The Freewheelin' Bob Dylan",
                            "release_date": "1963-05-27",
                            "total_tracks": 13
                          },
                          "artists": [
                            {
                              "name": "Bob Dylan"
                            }
                          ]
                        },
                        {
                          "duration_ms": 240000,
                          "name": "Hotel California",
                          "popularity": 91,
                          "album": {
                            "name": "Hotel California",
                            "release_date": "1976-12-08",
                            "total_tracks": 9
                          },
                          "artists": [
                            {
                              "name": "Eagles"
                            }
                          ]
                        },
                        {
                          "duration_ms": 195000,
                          "name": "Bad Guy",
                          "popularity": 89,
                          "album": {
                            "name": "When We All Fall Asleep, Where Do We Go?",
                            "release_date": "2019-03-29",
                            "total_tracks": 14
                          },
                          "artists": [
                            {
                              "name": "Billie Eilish"
                            }
                          ]
                        },
                        {
                          "duration_ms": 210500,
                          "name": "Smells Like Teen Spirit",
                          "popularity": 93,
                          "album": {
                            "name": "Nevermind",
                            "release_date": "1991-09-24",
                            "total_tracks": 12
                          },
                          "artists": [
                            {
                              "name": "Nirvana"
                            }
                          ]
                        },
                        {
                          "duration_ms": 205000,
                          "name": "Imagine",
                          "popularity": 98,
                          "album": {
                            "name": "Imagine",
                            "release_date": "1971-10-11",
                            "total_tracks": 10
                          },
                          "artists": [
                            {
                              "name": "John Lennon"
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


