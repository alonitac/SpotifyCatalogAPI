package com.example.catalog.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Collections;

@RestController
public class PracticeController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/capitalize")
    public String capitalizeName(String name) {
        return StringUtils.capitalize(name);
    }


    /**
     * @return sample sorted songs list
     */
    @GetMapping("/sampleSongsNames")
    public List<String> getSampleSongs() {
        List<String> songNames = Arrays.asList(
                "Kill Bill", "Daydreaming", "Havana (feat. Young Thug)"
        );

        Collections.sort(songNames);

        return songNames;
    }


    /**
     * @return the song object with the highest popularity
     * @throws IOException
     */
    @GetMapping("/mostPopularSongs")
    public Map<String, Object> getMostPopularSongs() throws IOException {
        ClassPathResource resource = new ClassPathResource("data/popular_songs.json");
        JsonNode songsNode = objectMapper.readTree(resource.getFile());
        List<Map<String, Object>> songsList = objectMapper.convertValue(songsNode, List.class);
        Map<String, Object> mostPopularSong = null;
        int maxPopularity = Integer.MIN_VALUE;

        for (Map<String, Object> song : songsList) {
            int popularity = (Integer) song.get("popularity");
            if (popularity > maxPopularity) {
                maxPopularity = popularity;
                mostPopularSong = song;
            }
        }

        if (mostPopularSong == null) {
            throw new IllegalStateException("No songs found");
        }


        return mostPopularSong;

    }
}
