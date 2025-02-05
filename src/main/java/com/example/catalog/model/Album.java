package com.example.catalog.model;

import com.example.catalog.model.Image;
import com.example.catalog.model.Track;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Album {
    private String id;
    private String name;
    private String uri;
    private String release_date;
    private int total_tracks;
    private List<Image> images;
    public Album(){

    }
    public Album(String id, String name){
        this.id=id;
        this.name=name;
    }


    public Album(String id, String name, String uri, String release_date, int total_tracks, List<Image> images, List<Track> tracks) {
        this.id = id;
        this.name = name;
        this.uri = uri;
        this.release_date = release_date;
        this.total_tracks = total_tracks;
        this.images = images;
        this.tracks = tracks;
    }

    private List<Track> tracks;


    // Getters and Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setTotal_tracks(int total_tracks) {
        this.total_tracks = total_tracks;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public String getRelease_date() {
        return release_date;
    }

    public int getTotal_tracks() {
        return total_tracks;
    }

    public List<Image> getImages() {
        return images;
    }

    public List<Track> getTracks() {
        return tracks;
    }
}
