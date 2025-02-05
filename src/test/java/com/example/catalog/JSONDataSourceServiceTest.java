package com.example.catalog;

import com.example.catalog.controller.ArtistController;
import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import com.example.catalog.services.JSONDataSourceService;
import com.example.catalog.services.SpotifyAPIDataSources;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class JSONDataSourceServiceTest {
    private String albumId = "4yP0hdKOZPNshxUOjY0cZj";
    @Autowired
    private ArtistController artistController;

    @MockBean
    //private SpotifyAPIDataSources spotifyAPIDataSources;  // Mocking Spotify API

    //@Qualifier("jsonDataSourceService")
     @Autowired
    private JSONDataSourceService jsonDataSourceService;

    @Test //1
    public void testGetAllAlbums() throws IOException {
        List<Album> albums=jsonDataSourceService.getAllAlbums();
        assertNotNull(albums, "Artist list should not be null");
        assertFalse(albums.isEmpty(), "Artist list should not be empty");

    }
    @Test
    public void testGetAlbumById() throws IOException {//2
        String albumId = "4yP0hdKOZPNshxUOjY0cZj";
        Album album = jsonDataSourceService.getAlbumById(albumId);
        assertNotNull(album, "Album should not be null");
        assertEquals(albumId, album.getId(), "Album id should match");
    }

    // Test for creating a new album
    @Test
    public void testCreateAlbum() throws IOException {//3
        String id="322456";
        String name="maias";
        Album newAlbum = new Album(id,name);
        Album createdAlbum = jsonDataSourceService.createAlbum(newAlbum);
        assertNotNull(createdAlbum, "Created album should not be null");
        assertEquals("maias", createdAlbum.getName(), "Album name should match");
    }

    // Test for updating an existing album
    @Test
    public void testUpdateAlbum() throws IOException {//4
        String albumId = "322456";

        Album updatedAlbum = new Album(albumId,"maias23");
        Album result = jsonDataSourceService.updateAlbum(albumId, updatedAlbum);
        assertNotNull(result, "Updated album should not be null");
        assertEquals("maias23", result.getName(), "Album name should match");
    }

    // Test for deleting an album
    @Test
    public void testDeleteAlbum() throws IOException {//5
        String albumId = "322456";
        boolean result = jsonDataSourceService.deleteAlbum(albumId);
        assertTrue(result, "Album should be deleted successfully");
        Album deletedAlbum = jsonDataSourceService.getAlbumById(albumId);
        assertNull(deletedAlbum, "Deleted album should not be found");
    }
    @Test
    public void testGetTracks() throws IOException {//6
        // Initially, the test album should have an empty track list.
        List<Track> tracks = jsonDataSourceService.getTracks("4yP0hdKOZPNshxUOjY0cZj");
        assertNotNull(tracks, "Tracks list should not be null");
        // Depending on your JSON data and the test setup, this might be empty.
    }
    @Test
    public void testAddAndGetTrack() throws IOException {//7
        // Create a new track
        Track newTrack = new Track("track1", "Test Track");
        Album albumAfterAdd = jsonDataSourceService.addTrack(albumId, newTrack);
        assertNotNull(albumAfterAdd, "Album should be returned after adding a track");

        // Retrieve tracks and verify that the new track is present
        List<Track> tracks = jsonDataSourceService.getTracks(albumId);
        assertTrue(tracks.stream().anyMatch(t -> "track1".equals(t.getId())),
                "Track with id 'track1' should be present in the album");
    }
    @Test
    public void testUpdateTrack() throws IOException {//8
        // First, add a track to update.
        Track track = new Track("trackToUpdate", "Old Track Name");
        jsonDataSourceService.addTrack(albumId, track);

        // Now update the track's name.
        Track updatedTrack = new Track("trackToUpdate", "New Track Name");
        Track result = jsonDataSourceService.updateTrack(albumId, "trackToUpdate", updatedTrack);
        assertNotNull(result, "Updated track should not be null");
        assertEquals("New Track Name", result.getName(), "Track name should be updated");

        // Verify the change in the album
        List<Track> tracks = jsonDataSourceService.getTracks(albumId);
        assertTrue(tracks.stream().anyMatch(t -> "New Track Name".equals(t.getName())),
                "Updated track name should be reflected in the album");
    }
    @Test
    public void testDeleteTrack() throws IOException {//9
        // Add a track to then delete.
        Track track = new Track("trackToDelete", "Track to be deleted");
        jsonDataSourceService.addTrack(albumId, track);

        boolean deleted = jsonDataSourceService.deleteTrack(albumId, "trackToDelete");
        assertTrue(deleted, "Track should be deleted successfully");

        // Verify the track is no longer in the album.
        List<Track> tracks = jsonDataSourceService.getTracks(albumId);
        assertFalse(tracks.stream().anyMatch(t -> "trackToDelete".equals(t.getId())),
                "Deleted track should not be present in the album");
    }
    @Test//10
    public void testGetAllArtists() throws IOException {
        // When: we retrieve all artists
        List<Artist> artists = jsonDataSourceService.getAllArtists();


        // Then: the list should not be null and contain at least one artist
        assertNotNull(artists, "Artist list should not be null");
        assertFalse(artists.isEmpty(), "Artist list should not be empty");
    }
    @Test
    public void testGetArtistById() throws Exception {//11
        String artistId = "6eUKZXaKkcviH0Ku9w2n3V";

        // When: we retrieve the artist by id
        Artist artist = jsonDataSourceService.getArtistById(artistId);
        // Then: the returned artist should not be null and have the expected properties.
        assertNotNull(artist, "Artist should not be null");
        assertEquals(artistId, artist.getId(), "Artist id should match");
        assertEquals("Ed Sheeran", artist.getName(), "Artist name should match");
        // You can add further assertions to validate other properties like followers, genres, etc.
    }

    @Test
    public void testGetArtistByIdNotFound() throws IOException {
        String artistId = "non-existent-id";

        // When: we retrieve an artist with a non-existent ID
        Artist artist = jsonDataSourceService.getArtistById(artistId);

        // Then: the returned artist should be null
        assertNull(artist, "Artist should be null for a non-existent ID");
    }
    @Test
    public void testAddArtist() throws IOException {//12
        // Given: an artist to add
        Artist newArtist = new Artist("123", "New Artist");

        // When: we add the artist
        jsonDataSourceService.addArtist(newArtist);

        // Then: the artist should be in the list
        Artist artist = jsonDataSourceService.getArtistById("123");
        assertNotNull(artist, "New artist should be added and not be null");
        assertEquals("New Artist", artist.getName(), "Artist name should match");
    }

    @Test
    public void testUpdateArtist() throws IOException {//13
        // Given: an artist to update
        String artistId = "1Xyo4u8uXC1ZmMpatF05PJ";
        Artist updatedArtist = new Artist(artistId, "Updated Artist Name");

        // When: we update the artist
        jsonDataSourceService.updateArtist(artistId, updatedArtist);

        // Then: the artist details should be updated
        Artist artist = jsonDataSourceService.getArtistById(artistId);
        assertNotNull(artist, "Updated artist should not be null");
        assertEquals("Updated Artist Name", artist.getName(), "Artist name should match the updated value");
    }

    @Test
    public void testDeleteArtist() throws IOException {//14
        // Given: an artist to delete
        String artistId = "123"; // Assume this artist was added in the testAddArtist() method
        Artist artistToDelete = new Artist(artistId, "Artist to be Deleted");
        jsonDataSourceService.addArtist(artistToDelete);

        // When: we delete the artist
        boolean result = jsonDataSourceService.deleteArtist(artistId);

        // Then: the result should be true and the artist should no longer exist
        assertTrue(result, "Artist should be deleted successfully");
        Artist deletedArtist = jsonDataSourceService.getArtistById(artistId);
        assertNull(deletedArtist, "Deleted artist should not be found");
    }
    @Test//15
    public void testGetAllSongs() throws IOException {//15
        // When: we retrieve all artists
        List<Song> songs = jsonDataSourceService.getAllSongs();
        // Then: the list should not be null and contain at least one artist
        assertNotNull(songs, "songs list should not be null");
        assertFalse(songs.isEmpty(), "songs list should not be empty");
    }
    @Test//15
    public void testGetSongById() throws IOException {//15
        String songId = "0VjIjW4GlUZAMYd2vXMi3b";

        // When: we retrieve the artist by id
        Song song1 = jsonDataSourceService.getSongById(songId);
        // Then: the returned artist should not be null and have the expected properties.
        assertNotNull(song1, "Song should not be null");
        assertEquals(songId, song1.getId(), "Song id should match");
        assertEquals("Blinding Lights",song1.getName(), "Song name should match");
        // You can add further assertions to validate other properties like followers, genres, etc.
    }
    @Test
    public void testCreateSong() throws IOException {
        // Given: an artist to add
       Song newSong = new Song("hkefbbef37456", "Hello its me");

        List<Song> songs=jsonDataSourceService.getAllSongs();
        jsonDataSourceService.createSong(newSong);


        // Then: the artist should be in the list
        Song song = jsonDataSourceService.getSongById("hkefbbef37456");
        assertNotNull(song, "New artist should be added and not be null");
        assertEquals("Hello its me", song.getName());

    }





}
