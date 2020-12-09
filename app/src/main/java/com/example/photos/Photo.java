package com.example.photos;

import android.net.Uri;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Photo implements Serializable {
    String location;
    String caption;
    ArrayList<String> tags;

    public Photo(Uri uri){
        this.location = uri.toString();
        this.caption = new File(uri.getPath()).getName();
        this.tags = new ArrayList<String>();
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

    public ArrayList<String> getTags() {
        return this.tags;
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public String toString() {
        return this.caption;
    }

}
