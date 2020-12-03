package com.example.photos;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {
    String name;
    ArrayList<Photo> photos;

    public Album(String name) {
        this.name = name;
        this.photos = new ArrayList<Photo>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }

    public String toString() {
        return this.name;
    }

    public int numberOfPhotos(){
        return this.photos.size();
    }



}
