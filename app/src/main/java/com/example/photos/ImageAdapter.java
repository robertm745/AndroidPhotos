package com.example.photos;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Album album;
    private Uri[] uri;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return album.numberOfPhotos();
    }

    public Object getItem(int position) {
        return album.getPhotos().get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageURI(Uri.parse(album.getPhotos().get(position).getLocation()));


        return imageView;
    }

     public void setAlbum(Album album){
        this.album = album;
    }


}
