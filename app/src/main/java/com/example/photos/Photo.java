package com.example.photos;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Photo implements Serializable {
    String location;
    String caption;
    ArrayList<String> locationTags;
    ArrayList<String> personTags;

    public Photo(Uri uri){
        this.location = uri.toString();
        this.caption = new File(uri.getPath()).getName();
        this.locationTags = new ArrayList<String>();
        this.personTags = new ArrayList<String>();
    }

    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public ArrayList<String> getLocationTags() {
        return locationTags;
    }

    public ArrayList<String> getPersonTags() {
        return personTags;
    }

    public void addPersonTag(String tag) {
        this.personTags.add(tag);
    }

    public void addLocationTag(String tag) {
        this.locationTags.add(tag);
    }

    public String toString() {
        return this.caption;
    }

}
