package com.example.catalog.services;
import com.example.catalog.model.Album;
import com.example.catalog.model.Artist;
import com.example.catalog.model.Song;
import com.example.catalog.model.Track;

import java.io.IOException;
import java.util.List;

public interface DataSourceService {
    Artist getArtistById(String id) throws IOException;//1
    List<Artist> getAllArtists() throws IOException;//2
    Artist addArtist(Artist artist) throws IOException;//3
    Artist updateArtist(String id, Artist artist) throws IOException;//4
    boolean deleteArtist(String id) throws IOException;//5
    public List<Album> getAllAlbums() throws IOException;//6
    public Album getAlbumById(String id) throws IOException;//7
    public Album createAlbum(Album album) throws IOException;//8
    public Album updateAlbum(String id, Album updatedAlbum) throws IOException;//9
    public boolean deleteAlbum(String id) throws IOException;//10
    public List<Track> getTracks(String albumId) throws IOException;//11
    public Album addTrack(String albumId, Track track) throws IOException;//12
    public Track updateTrack(String albumId, String trackId, Track updatedTrack) throws IOException;//13
    public boolean deleteTrack(String albumId, String trackId) throws IOException;//14
    List<Song> getAllSongs() throws IOException;//15
    Song getSongById(String id) throws IOException;//16
    Song createSong(Song song) throws IOException;//17
    void updateSong(String id, Song song) throws IOException;//18
    boolean deleteSong(String id) throws IOException;//19




}
