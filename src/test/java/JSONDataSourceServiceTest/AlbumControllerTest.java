package JSONDataSourceServiceTest;

import com.example.catalog.controller.AlbumController;
import com.example.catalog.model.Album;
import com.example.catalog.model.Track;
import com.example.catalog.services.JSONDataSourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlbumControllerTest {
    private AlbumController albumController;

    @BeforeEach
    void setUp() {
        // Initialize the service with actual JSON data
        JSONDataSourceService jsonDataSourceService = new JSONDataSourceService();
        albumController = new AlbumController(jsonDataSourceService);
    }

    // Test for getting all albums
    @Test
    void testGetAllAlbums() throws IOException {
        ResponseEntity<List<Album>> response = albumController.getAllAlbums();
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(200, response.getStatusCodeValue());  // Check for OK status
    }

    // Test for getting an album by ID (existing ID)
    @Test
    void testGetAlbumById() throws IOException {
        String albumId = "4yP0hdKOZPNshxUOjY0cZj"; // Assuming ID exists in the JSON data
        ResponseEntity<Album> response = albumController.getAlbumById(albumId);
        assertNotNull(response.getBody());
        assertEquals(albumId, response.getBody().getId());
        assertEquals(200, response.getStatusCodeValue());  // Check for OK status
    }

    // Test for getting an album by ID (non-existent ID)
    @Test
    void testGetAlbumByIdNotFound() throws IOException {
        String albumId = "nonexistent_id"; // Non-existent ID
        ResponseEntity<Album> response = albumController.getAlbumById(albumId);
        assertNull(response.getBody());  // Should return null for non-existent album
        assertEquals(404, response.getStatusCodeValue());  // Check for Not Found status
    }

    // Test for creating a new album
    @Test
    void testCreateAlbum() throws IOException {
        String id = "maias123";
        String name = "maias";
        Album newAlbum = new Album(id, name);
        ResponseEntity<Album> response = albumController.createAlbum(newAlbum);
        assertNotNull(response.getBody());
        assertEquals(newAlbum.getName(), response.getBody().getName());
        assertEquals(200, response.getStatusCodeValue());  // Check for OK status
    }

    // Test for creating a new album with invalid input (Bad Request)
    @Test
    void testCreateAlbumBadRequest() throws IOException {
        Album newAlbum = new Album(null, ""); // Invalid album (ID is null, name is empty)
        ResponseEntity<Album> response = albumController.createAlbum(newAlbum);
        assertEquals(400, response.getStatusCodeValue());  // Check for Bad Request status
    }

    // Test for updating an existing album
    @Test
    void testUpdateAlbum() throws IOException {
        String albumId = "4yP0hdKOZPNshxUOjY0cZj"; // Assuming this album exists
        String newName = "Updated Album Name";
        Album updatedAlbum = new Album(albumId, newName);
        ResponseEntity<Album> response = albumController.updateAlbum(albumId, updatedAlbum);
        assertNotNull(response.getBody());
        assertEquals(newName, response.getBody().getName());
        assertEquals(200, response.getStatusCodeValue());  // Check for OK status
    }

    // Test for updating a non-existent album (Not Found)
    @Test
    void testUpdateAlbumNotFound() throws IOException {
        String albumId = "nonexistent_id"; // Non-existent ID
        Album updatedAlbum = new Album(albumId, "Updated Album");
        ResponseEntity<Album> response = albumController.updateAlbum(albumId, updatedAlbum);
        assertEquals(404, response.getStatusCodeValue());  // Check for Not Found status
    }

    // Test for deleting an existing album
    @Test
    void testDeleteAlbum() throws IOException {
        String albumId = "maias123"; // Assuming ID exists in the JSON data
        ResponseEntity<Void> response = albumController.deleteAlbum(albumId);
        assertEquals(204, response.getStatusCodeValue());  // Check for No Content status
    }

    // Test for deleting a non-existent album (Not Found)
    @Test
    void testDeleteAlbumNotFound() throws IOException {
        String albumId = "nonexistent_id"; // Non-existent ID
        ResponseEntity<Void> response = albumController.deleteAlbum(albumId);
        assertEquals(404, response.getStatusCodeValue());  // Check for Not Found status
    }

    // Test for getting album tracks (existing album)
    @Test
    void testGetAlbumTracks() throws IOException {
        String albumId = "3T4tUhGYeRNVUGevb0wThu"; // Assuming this album exists
        ResponseEntity<List<Track>> response = albumController.getAlbumTracks(albumId);
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(200, response.getStatusCodeValue());  // Check for OK status
    }

    // Test for getting tracks for a non-existent album (Not Found)
    @Test
    void testGetAlbumTracksNotFound() throws IOException {
        String albumId = "nonexistent_id"; // Non-existent ID
        ResponseEntity<List<Track>> response = albumController.getAlbumTracks(albumId);
        assertNull(response.getBody());
        assertEquals(404, response.getStatusCodeValue());  // Check for Not Found status
    }

    // Test for adding a track to an album
    @Test
    void testAddTrackToAlbum() throws IOException {
        String albumId = "3T4tUhGYeRNVUGevb0wThu"; // Assuming this album exists
        Track newTrack = new Track("New Track", "300");
        ResponseEntity<Album> response = albumController.addTrackToAlbum(albumId, newTrack);
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getTracks().contains(newTrack));
        assertEquals(200, response.getStatusCodeValue());
        albumController.deleteTrack(albumId,"New Track");
    }

    // Test for adding a track to a non-existent album (Not Found)
    @Test
    void testAddTrackToAlbumNotFound() throws IOException {
        String albumId = "nonexistent_id"; // Non-existent ID
        Track newTrack = new Track("New Track", "300");
        ResponseEntity<Album> response = albumController.addTrackToAlbum(albumId, newTrack);
        assertEquals(404, response.getStatusCodeValue());  // Check for Not Found status
    }

    // Test for updating a track in an album
    @Test
    void testUpdateTrack() throws IOException {
        String albumId = "3T4tUhGYeRNVUGevb0wThu"; // Assuming this album exists

        Track testTrack = new Track("test track", "320");
        albumController.addTrackToAlbum(albumId,testTrack);
        Track updatedTrack=new Track("updated track","330");
        ResponseEntity<Track> response = albumController.updateTrack(albumId, "test track", updatedTrack);
        assertNotNull(response.getBody());
        assertEquals(updatedTrack.getName(), response.getBody().getName());
        assertEquals(200, response.getStatusCodeValue());  // Check for OK status
        albumController.deleteTrack(albumId,"updated track");
        albumController.addTrackToAlbum(albumId,testTrack);
    }

    // Test for updating a non-existent track (Not Found)
    @Test
    void testUpdateTrackNotFound() throws IOException {
        String albumId = "3T4tUhGYeRNVUGevb0wThu"; // Assuming this album exists
        String trackId = "nonexistent_track";  // Non-existent track ID
        Track updatedTrack = new Track("Updated Track", "320");
        ResponseEntity<Track> response = albumController.updateTrack(albumId, trackId, updatedTrack);
        assertEquals(404, response.getStatusCodeValue());  // Check for Not Found status
    }

    // Test for deleting a track from an album
    @Test
    void testDeleteTrack() throws IOException {
        Track testsTrack=new Track( "2K9Ovn1o2bTGqbsABGC6m3","testedTrack");
        albumController.addTrackToAlbum("5658aM19fA3JVwTK6eQX70",testsTrack);

        String albumId = "5658aM19fA3JVwTK6eQX70"; // Assuming this album exists
        String trackId = "2K9Ovn1o2bTGqbsABGC6m3";  // Assuming this track exists
        ResponseEntity<Void> response = albumController.deleteTrack(albumId, trackId);
        assertEquals(200, response.getStatusCodeValue());  // Check deleted track
    }

    // Test for deleting a non-existent track (Not Found)
    @Test
    void testDeleteTrackNotFound() throws IOException {
        String albumId = "5658aM19fA3JVwTK6eQX70"; // Assuming this album exists
        String trackId = "nonexistent_track";  // Non-existent track ID
        ResponseEntity<Void> response = albumController.deleteTrack(albumId, trackId);
        assertEquals(404, response.getStatusCodeValue());  // Check for Not Found status
    }

    // Test for handling an internal server error
    @Test
    void testInternalServerError() throws IOException {
        // Simulate an internal server error (e.g., network or database issues)
        JSONDataSourceService mockService = new JSONDataSourceService() {
            @Override
            public List<Album> getAllAlbums() throws IOException {
                throw new IOException("Internal server error");
            }
        };
        AlbumController mockAlbumController = new AlbumController(mockService);
        ResponseEntity<List<Album>> response = mockAlbumController.getAllAlbums();
        assertEquals(500, response.getStatusCodeValue());  // Check for Internal Server Error status
    }
}
