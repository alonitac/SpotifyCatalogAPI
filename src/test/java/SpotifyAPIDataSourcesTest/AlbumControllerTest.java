package SpotifyAPIDataSourcesTest;

import com.example.catalog.controller.AlbumController;
import com.example.catalog.model.Album;
import com.example.catalog.model.Track;
import com.example.catalog.services.SpotifyAPIDataSources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AlbumControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SpotifyAPIDataSources spotifyAPIDataSources;

    @InjectMocks
    private AlbumController albumController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(albumController).build();
    }

    @Test
    void testGetAllAlbums() throws Exception {
        Album album = new Album();
        album.setId("1");
        album.setName("Test Album");

        when(spotifyAPIDataSources.getAllAlbums()).thenReturn(List.of(album));

        mockMvc.perform(get("/albums")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Test Album"));
    }

    @Test
    void testGetAlbumById() throws Exception {
        Album album = new Album();
        album.setId("1");
        album.setName("Test Album");

        when(spotifyAPIDataSources.getAlbumById("1")).thenReturn(album);

        mockMvc.perform(get("/albums/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test Album"));
    }

    @Test
    void testGetTracks() throws Exception {
        Track track = new Track("101","Test Track");


        when(spotifyAPIDataSources.getTracks("1")).thenReturn(List.of(track));

        mockMvc.perform(get("/albums/1/tracks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("101"))
                .andExpect(jsonPath("$[0].name").value("Test Track"));

    }
}
