package JSONDataSourceServiceTest;

import com.example.catalog.controller.SongController;
import com.example.catalog.model.Song;
import com.example.catalog.services.JSONDataSourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SongControllerTest {
    private SongController songController;

    @BeforeEach
    void setUp() {
        // Initialize the service with actual JSON data
        JSONDataSourceService jsonDataSourceService = new JSONDataSourceService();
        songController = new SongController(jsonDataSourceService);
    }

    // Test for getting all songs
    @Test
    void testGetAllSongs() throws IOException {
        ResponseEntity<List<Song>> response = songController.getAllSongs();
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(200, response.getStatusCodeValue());  // Check for OK status
    }

    // Test for getting a song by ID (existing ID)
    @Test
    void testGetSongById() throws IOException {
        String songId = "0VjIjW4GlUZAMYd2vXMi3b"; // Assuming ID exists in the JSON data
        ResponseEntity<Song> response = songController.getSongById(songId);
        assertNotNull(response.getBody());
        assertEquals(songId, response.getBody().getId());
        assertEquals(200, response.getStatusCodeValue());  // Check for OK status
    }

    // Test for getting a song by ID (non-existent ID)
    @Test
    void testGetSongByIdNotFound() throws IOException {
        String songId = "nonexistent_id"; // Non-existent ID
        ResponseEntity<Song> response = songController.getSongById(songId);
        assertNull(response.getBody());  // Should return null for non-existent song
        assertEquals(404, response.getStatusCodeValue());  // Check for Not Found status
    }

    // Test for creating a new song
    @Test
    void testCreateSong() throws IOException {
        String id = "newSong123";
        String name = "New Song";
        Song newSong = new Song(id, name);
        ResponseEntity<Song> response = songController.createSong(newSong);
        assertNotNull(response.getBody());
        assertEquals(newSong.getName(), response.getBody().getName());
        assertEquals(200, response.getStatusCodeValue());
        songController.deleteSong(id);// Check for OK status
    }

    // Test for creating a new song with invalid input (Bad Request)
    @Test
    void testCreateSongBadRequest() throws IOException {
        Song newSong = new Song(null, ""); // Invalid song (ID is null, name is empty)
        ResponseEntity<Song> response = songController.createSong(newSong);
        assertEquals(400, response.getStatusCodeValue());  // Check for Bad Request status
    }

    // Test for updating an existing song
    @Test
    void testUpdateSong() throws IOException {
        String id = "newSong123";
        String name = "New Song";
        Song newSong = new Song(id, name);
        songController.createSong(newSong);
        String songId = "song123"; // Assuming this song exists
        String newName = "Updated Song Name";
        Song updatedSong = new Song(songId, newName);
        ResponseEntity<Song> response = songController.updateSong(id, updatedSong);
        assertNotNull(response.getBody());
        assertEquals(newName, response.getBody().getName());
        assertEquals(200, response.getStatusCodeValue());
        songController.deleteSong(songId);// Check for OK status

    }

    // Test for updating a non-existent song (Not Found)
    @Test
    void testUpdateSongNotFound() throws IOException {
        String songId = "nonexistent_id"; // Non-existent ID
        Song updatedSong = new Song(songId, "Updated Song");
        ResponseEntity<Song> response = songController.updateSong(songId, updatedSong);
        assertEquals(404, response.getStatusCodeValue());  // Check for Not Found status
    }

    // Test for deleting an existing song
    @Test
    void testDeleteSong() throws IOException {
        String songId = "newSong123"; //
        String name = "New Song";
        Song newSong = new Song(songId, name);
        songController.createSong(newSong);
        // Assuming ID exists in the JSON data
        ResponseEntity<Void> response = songController.deleteSong(songId);
        assertEquals(200, response.getStatusCodeValue());  // Check for No Content status
    }

    // Test for deleting a non-existent song (Not Found)
    @Test
    void testDeleteSongNotFound() throws IOException {
        String songId = "nonexistent_id"; // Non-existent ID
        ResponseEntity<Void> response = songController.deleteSong(songId);
        assertEquals(404, response.getStatusCodeValue());  // Check for Not Found status
    }

    // Test for handling an internal server error
    @Test
    void testInternalServerError() throws IOException {
        // Simulate an internal server error (e.g., network or database issues)
        JSONDataSourceService mockService = new JSONDataSourceService() {
            @Override
            public List<Song> getAllSongs() throws IOException {
                throw new IOException("Internal server error");
            }
        };
        SongController mockSongController = new SongController(mockService);
        ResponseEntity<List<Song>> response = mockSongController.getAllSongs();
        assertEquals(500, response.getStatusCodeValue());  // Check for Internal Server Error status
    }
}
