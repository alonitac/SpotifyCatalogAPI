package com.example.catalog.services;

import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service("jsonDataSourceService")
public class JSONDataSourceService implements DataSourceService {
    private static final String ARTISTS_FILE_PATH = "dataTest/popular_artists_test.json";
    private static final String ALBUMS_FILE_PATH = "dataTest/albums_test.json";
    private static final String SONGS_FILE_PATH = "dataTest/popular_songs_test.json";

    //private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<Album> getAllAlbums() throws IOException {//1
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode albums=loadJsonData(ALBUMS_FILE_PATH);
        List<Album> albumsList=new ArrayList<>();
        for(JsonNode albumNode: albums)
        {
            Album album=objectMapper.treeToValue(albumNode, Album.class);
            System.out.println(album.getId());
            albumsList.add(album);
        }



        return albumsList;

    }

    @Override
    public Album getAlbumById(String id) throws IOException {//2
        List<Album> albums=getAllAlbums();
        for(Album album:albums)
        {
            if(album.getId().equals(id))
            {
                return album;
            }
        }
        return null;
    }

    @Override
    public Album createAlbum(Album album) throws IOException {//3
        List<Album> albums = getAllAlbums();
        albums.add(album);
        saveAlbums(albums);
        return album;
    }

    @Override
    public Album updateAlbum(String id, Album updatedAlbum) throws IOException {//4
        List<Album> albums = getAllAlbums();
        for (int i = 0; i < albums.size(); i++) {
            if (albums.get(i).getId().equals(id)) {
                albums.set(i, updatedAlbum);
                saveAlbums(albums);
                return updatedAlbum;
            }
        }
        return null;
    }

    @Override
    public boolean deleteAlbum(String id) throws IOException {//5
        List<Album> albums = getAllAlbums();
        boolean removed = albums.removeIf(album -> album.getId().equals(id));
        if (removed) {
            saveAlbums(albums);
        }
        return removed;
    }

    @Override
    public List<Track> getTracks(String albumId) throws IOException {//6
        Album album = getAlbumById(albumId);
        return album != null ? album.getTracks() : List.of();
    }

    @Override
    public Album addTrack(String albumId, Track track) throws IOException {//7
        List<Album> albums = getAllAlbums();
        for (Album album : albums) {
            if (album.getId().equals(albumId)) {
                album.getTracks().add(track);
                saveAlbums(albums);
                return album;
            }
        }
        return null;
    }

    @Override
    public Track updateTrack(String albumId, String trackId, Track updatedTrack) throws IOException {//8
        List<Album> albums = getAllAlbums();
        for (Album album : albums) {
            if (album.getId().equals(albumId)) {
                for (int i = 0; i < album.getTracks().size(); i++) {
                    if (album.getTracks().get(i).getId().equals(trackId)) {
                        album.getTracks().set(i, updatedTrack);
                        saveAlbums(albums);
                        return updatedTrack;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean deleteTrack(String albumId, String trackId) throws IOException {//9
        List<Album> albums = getAllAlbums();
        for (Album album : albums) {
            if (album.getId().equals(albumId)) {
                boolean removed = album.getTracks().removeIf(track -> track.getId().equals(trackId));
                if (removed) {
                    saveAlbums(albums);
                    return true;
                }
            }
        }
        return false;
    }
    private void saveAlbums(List<Album> albums) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new ClassPathResource(ALBUMS_FILE_PATH).getFile();
        objectMapper.writeValue(file, albums);
    }


  //  private static final String FILE_PATH = "data/popular_artists.json";

    @Override
    public Artist getArtistById(String id) throws IOException {//10
        List<Artist> artists = getAllArtists(); // Fetch all artists

        // Search for the artist with the given ID
        for (Artist artist : artists) {
            if (artist.getId().equals(id)) {
                return artist; // Return the artist if found
            }
        }

        // Artist not found
        System.out.println("Artist not found: " + id);
        return null;
    }

    @Override
    public List<Artist> getAllArtists() throws IOException {//11
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode artists = loadJsonData(ARTISTS_FILE_PATH);
        List<Artist> artistsList = new ArrayList<>();
        // Iterate over the values of the root node
        for (JsonNode artistNode : artists) {
            Artist artist = objectMapper.treeToValue(artistNode, Artist.class);
            artistsList.add(artist);
        }
        return artistsList;

    }

    @Override
    public Artist addArtist(Artist artist) throws IOException {//12
        List<Artist> artists = getAllArtists();
        artists.add(artist);
        saveJsonData(artists);

        return artist;
    }

    @Override
    public Artist updateArtist(String id, Artist artist) throws IOException {//13
      System.out.println("hello json ");
        List<Artist> artists = getAllArtists();  // Retrieve all artists


        for (int i = 0; i < artists.size(); i++) {
            Artist art = artists.get(i);
            if (art.getId().equals(id)) {
                // If the artist is found, update the artist at this index
                artists.set(i, artist);  // Update the artist in the list
                saveJsonData(artists);  // Save the updated list to the data source

                return artist;  // Return the updated artist
            }
        }


        return null;
    }

    @Override
    public boolean deleteArtist(String id) throws IOException {//14
        // Fetch the current list of artists
        List<Artist> artists = getAllArtists();

        // Try to remove the artist with the given ID
        boolean removed = artists.removeIf(a -> a.getId().equals(id));

        // If the artist was removed, save the updated list and return true
        if (removed) {
            saveJsonData(artists);  // Save the updated list back to the JSON file
            return true; // Artist was found and deleted
        }

        // If no artist was found with the provided ID, return false
        return false;
    }
    @Override
    public List<Song> getAllSongs() throws IOException {//15
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode songs = loadJsonData(SONGS_FILE_PATH);
        List<Song> songsList = new ArrayList<>();

        // Iterate over the values of the root node
        for (JsonNode songtNode : songs) {
            Song song = objectMapper.treeToValue(songtNode, Song.class);
            songsList.add(song);
        }
        return songsList;


    }

    @Override
    public Song getSongById(String id) throws IOException {//16
        List<Song> songs=getAllSongs();
        for(Song song:songs)
        {
            if(song.getId().equals(id))
            {
                return song;
            }
        }
        return null;
    }

    @Override
    public Song createSong(Song song) throws IOException {//17
        List<Song> songs = getAllSongs();
        int flag=0;

        for(Song son:songs)
        {
            if(son.getId().equals(song.getId()))
            {
                flag=1;
            }

        }
        if(flag==0)
        {
            songs.add(song);
            saveJsonDataSongs(songs);
        }
        else{
            System.out.println("is already exist");
        }



        return song;
    }

    @Override
    public void updateSong(String id, Song song) throws IOException {//18
        List<Song> songs = getAllSongs();  // Retrieve all artists

        // Loop through the list of artists to find the one with the matching ID
        for (int i = 0; i < songs.size(); i++) {
            Song son = songs.get(i);
            if (son.getId().equals(id)) {
                // If the artist is found, update the artist at this index
                songs.set(i, song);
                saveJsonDataSongs(songs);  // Save the updated list to the data source
                return ;
            }
        }

    }

    @Override
    public boolean deleteSong(String id) throws IOException {//19
        // Fetch the current list of artists
        List<Song> songs = getAllSongs();

        // Try to remove the artist with the given ID
        boolean removed = songs.removeIf(a -> a.getId().equals(id));

        // If the artist was removed, save the updated list and return true
        if (removed) {
            saveJsonDataSongs(songs);  // Save the updated list back to the JSON file
            return true; // Artist was found and deleted
        }

        // If no artist was found with the provided ID, return false
        return false;
    }

    private JsonNode loadJsonData(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource(path);
        return objectMapper.readTree(resource.getFile());
    }
    private void saveJsonData(List<Artist> artists) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new ClassPathResource(ARTISTS_FILE_PATH).getFile();
        objectMapper.writeValue(file, artists);
    }
    private void saveJsonDataSongs(List<Song> songs) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new ClassPathResource(SONGS_FILE_PATH).getFile();
        objectMapper.writeValue(file, songs);
    }
}