package SpotifyAPIDataSourcesTest;

import com.example.catalog.controller.ArtistController;
import com.example.catalog.model.Artist;
import com.example.catalog.services.DataSourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ArtistControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DataSourceService dataSourceService;

    @InjectMocks
    private ArtistController artistController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(artistController).build();
    }

    @Test
    void testGetArtistById_Success() throws Exception {
        Artist mockArtist = new Artist("123", "Test Artist");
        when(dataSourceService.getArtistById("123")).thenReturn(mockArtist);

        mockMvc.perform(get("/artists/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("Test Artist"));
    }

    @Test
    void testGetArtistById_NotFound() throws Exception {
        when(dataSourceService.getArtistById("123")).thenReturn(null);

        mockMvc.perform(get("/artists/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllArtists_Success() throws Exception {
        List<Artist> mockArtists = Arrays.asList(
                new Artist("1", "Artist One"),
                new Artist("2", "Artist Two")
        );
        when(dataSourceService.getAllArtists()).thenReturn(mockArtists);

        mockMvc.perform(get("/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Artist One"))
                .andExpect(jsonPath("$[1].name").value("Artist Two"));
    }

    @Test
    void testAddArtist_Success() throws Exception {
        Artist mockArtist = new Artist("123", "New Artist");
        when(dataSourceService.addArtist(any(Artist.class))).thenReturn(mockArtist);

        mockMvc.perform(post("/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"123\",\"name\":\"New Artist\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.name").value("New Artist"));
    }

    @Test
    void testUpdateArtist_Success() throws Exception {
        Artist mockArtist = new Artist("123", "Updated Artist");
        when(dataSourceService.getArtistById("123")).thenReturn(mockArtist);
        when(dataSourceService.updateArtist(eq("123"), any(Artist.class))).thenReturn(mockArtist);

        mockMvc.perform(put("/artists/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"123\",\"name\":\"Updated Artist\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Artist"));
    }


}
