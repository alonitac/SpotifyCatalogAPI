package JSONDataSourceServiceTest;

import com.example.catalog.controller.ArtistController;

import com.example.catalog.model.Artist;
import com.example.catalog.services.JSONDataSourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArtistControllerTest {
    private ArtistController artistController;

    @BeforeEach
    void setUp() {
        // Initialize the service with actual JSON data
        JSONDataSourceService jsonDataSourceService = new JSONDataSourceService();
        artistController = new ArtistController(jsonDataSourceService);
    }

    // Test for getting all artists
    @Test
    void testGetAllArtists() throws IOException {
        ResponseEntity<List<Artist>> response = artistController.getAllArtists();
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
        assertEquals(200, response.getStatusCodeValue());  // Check for OK status
    }

    // Test for getting an artist by ID (existing ID)
    @Test
    void testGetArtistById() throws IOException {
        String artistId = "6eUKZXaKkcviH0Ku9w2n3V"; // Assuming ID exists in the JSON data
        ResponseEntity<Artist> response = artistController.getArtistById(artistId);
        assertNotNull(response.getBody());
        assertEquals(artistId, response.getBody().getId());
        assertEquals(200, response.getStatusCodeValue());  // Check for OK status
    }

    // Test for getting an artist by ID (non-existent ID)
    @Test
    void testGetArtistByIdNotFound() throws IOException {
        String artistId = "nonexistent_id"; // Non-existent ID
        ResponseEntity<Artist> response = artistController.getArtistById(artistId);
        assertNull(response.getBody());  // Should return null for non-existent artist
        assertEquals(404, response.getStatusCodeValue());  // Check for Not Found status
    }

    // Test for creating a new artist
    @Test
    void testCreateArtist() throws IOException {
        String id = "maias12345";
        String name = "Maias";
        Artist newArtist = new Artist(id, name);
        ResponseEntity<Artist> response = artistController.addArtist(newArtist);
        assertNotNull(response.getBody());
        assertEquals(newArtist.getName(), response.getBody().getName());
        assertEquals(200, response.getStatusCodeValue());  // Check for OK status
        artistController.deleteArtist(id);

    }

    // Test for creating a new artist with invalid input (Bad Request)
    @Test
    void testCreateArtistBadRequest() throws IOException {
        Artist newArtist = new Artist(null, ""); // Invalid artist (ID is null, name is empty)
        ResponseEntity<Artist> response = artistController.addArtist(newArtist);
        assertEquals(400, response.getStatusCodeValue());  // Check for Bad Request status
    }

    // Test for updating an existing artist
    @Test
    void testUpdateArtist() throws IOException {
        String artistId = "1"; // Assuming this artist exists
        String newName = "Test Artist";
        Artist testArtist = new Artist(artistId, newName);

        // Adding the artist first
        artistController.addArtist(testArtist);

        // Preparing the artist to update
        Artist updatedArtist = new Artist(artistId, "Updated Artist");

        // Trying to update the artist
        ResponseEntity<Artist> response = artistController.updateArtist(artistId, updatedArtist);
        assertNotNull(response.getBody());  // Make sure the response body isn't null
        assertEquals("Updated Artist", response.getBody().getName()); // Check if the name was updated
        assertEquals(200, response.getStatusCodeValue());  // Check for OK status

        // Clean up (optional)
        artistController.deleteArtist(artistId);
        artistController.addArtist(testArtist);  // Re-add the original artist
    }


    // Test for updating a non-existent artist (Not Found)
    @Test
    void testUpdateArtistNotFound() throws IOException {
        String artistId = "nonexistent_id"; // Non-existent ID
        Artist updatedArtist = new Artist(artistId, "Updated Artist");
        ResponseEntity<Artist> response = artistController.updateArtist(artistId, updatedArtist);
        assertEquals(404, response.getStatusCodeValue());  // Check for Not Found status
    }

    // Test for deleting an existing artist
    @Test
    void testDeleteArtist() throws IOException {
        String id = "maias12345";
        String name = "Maias";
        Artist newArtist = new Artist(id, name);
        artistController.addArtist(newArtist);
        ResponseEntity<Void> response = artistController.deleteArtist(id);
        assertEquals(200, response.getStatusCodeValue());  // Check for No Content status
    }

    // Test for deleting a non-existent artist (Not Found)
    @Test
    void testDeleteArtistNotFound() throws IOException {
        String artistId = "nonexistent_id"; // Non-existent ID
        ResponseEntity<Void> response = artistController.deleteArtist(artistId);
        assertEquals(404, response.getStatusCodeValue());  // Check for Not Found status
    }

    // Test for handling an internal server error
    @Test
    void testInternalServerError() throws IOException {
        // Simulate an internal server error (e.g., network or database issues)
        JSONDataSourceService mockService = new JSONDataSourceService() {
            @Override
            public List<Artist> getAllArtists() throws IOException {
                throw new IOException("Internal server error");
            }
        };
        ArtistController mockArtistController = new ArtistController(mockService);
        ResponseEntity<List<Artist>> response = mockArtistController.getAllArtists();
        assertEquals(500, response.getStatusCodeValue());  // Check for Internal Server Error status
    }
}
