package com.example.catalog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import java.util.List;


public class Artist {
    private String id;
    private String name;
    private int followers;
    private List<String> genres;
    private List<Image> images;

    public Artist(String number, String newArtist) {
        this.id=number;
        this.name=newArtist;
    }


    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Image> getImages() {
        return images;
    }

    private int popularity;
    private String uri;



    public Artist() {

    }



    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public int getFollowers() {
        return followers;
    }

    public Artist(String id, String name, int followers, List<String> genres, int popularity, String uri) {
        this.id = id;
        this.name = name;
        this.followers = followers;
        this.genres = genres;
        this.popularity = popularity;
        this.uri = uri;
    }

    public List<String> getGenres() {
        return genres;
    }

    public int getPopularity() {
        return popularity;
    }

    public String getUri() {
        return uri;
    }
}